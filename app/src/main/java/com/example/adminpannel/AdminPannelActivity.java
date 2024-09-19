package com.example.adminpannel;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminpannel.Models.CategoryModel;
import com.example.adminpannel.Models.Product_model;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminPannelActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private EditText titleEditText, descriptionEditText, priceEditText, quantity_id;
    private Spinner categorySpinner;
    private Button addCategoryButton,  salesBtn, addProductButton;
    private ImageView productImageView;
    private Uri imageUri;
    private ArrayAdapter<CategoryModel> categoryAdapter;
    private final List<CategoryModel> categories = new ArrayList<>();
    private String selectedCategoryId;
    private String selectedCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpannel);

        // Initialize Firebase components
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize UI elements
        titleEditText = findViewById(R.id.title_id);
        descriptionEditText = findViewById(R.id.description_id);
        priceEditText = findViewById(R.id.price_id);
        categorySpinner = findViewById(R.id.categorySpinner);
        addCategoryButton = findViewById(R.id.addCategoryBtn);
        addProductButton = findViewById(R.id.addProductBtn);
        productImageView = findViewById(R.id.image_id);
        quantity_id = findViewById(R.id.quantity_id); // total quantity
        salesBtn = findViewById(R.id.SalesBtn); // total quantity

        // Set up ArrayAdapter for Spinner
        categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Load categories from Firestore
        loadCategories();

        salesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPannelActivity.this, SoldPorductsActivity.class));

            }
        });
        // Set up button listeners
        addCategoryButton.setOnClickListener(view -> showAddCategoryDialog());
        addProductButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String price = priceEditText.getText().toString().trim();
            String quantity = quantity_id.getText().toString().trim();

            if (validateInputs(title, description, price)) {
                addProductWithCategory(selectedCategoryId, selectedCategoryName, title, description, price, quantity);
            }
        });

        productImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        // Spinner item selected listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = categories.get(position).getId(); // Get the selected category ID
                selectedCategoryName = categories.get(position).getName(); // Get the selected category ID
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = null; // Handle the case where no category is selected
            }
        });
    }

    // Function to show dialog to add a new category
    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Category");

        final EditText input = new EditText(this);
        input.setHint("Enter category name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialogInterface, i) -> {
            String category = input.getText().toString().trim();
            if (!TextUtils.isEmpty(category)) {
                addCategoryToFirebase(category);
            } else {
                Toast.makeText(AdminPannelActivity.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.show();
    }

    // Function to add category to Firebase
    private void addCategoryToFirebase(final String category) {
        String categoryId = UUID.randomUUID().toString(); // Generate a unique ID for the category

        firebaseFirestore.collection("categories").document(categoryId)
                .set(new CategoryModel(categoryId, category))  // Create a simple Category model to store
                .addOnSuccessListener(unused -> {
                    categories.add(new CategoryModel(categoryId, category));
                    categoryAdapter.notifyDataSetChanged();
                    Toast.makeText(AdminPannelActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(AdminPannelActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show());
    }

    // Function to load categories from Firebase
    private void loadCategories() {
        firebaseFirestore.collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categories.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        CategoryModel category = doc.toObject(CategoryModel.class);
                        if (category != null) {
                            categories.add(category);
                        }
                    }
                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(AdminPannelActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show());
    }

    // Input validation function
    private boolean validateInputs(String title, String description, String price) {
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError("Title is required");
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Description is required");
            return false;
        }

        if (TextUtils.isEmpty(price)) {
            priceEditText.setError("Price is required");
            return false;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedCategoryId == null) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (quantity_id == null) {
            Toast.makeText(this, "Please add quantity", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
// firestore data storing
    private void addProductWithCategory(String categoryId, String categoryname, String title,
                                        String description, String price, String quantity) {
        String id = UUID.randomUUID().toString(); // Generate new ID for each product

        if (imageUri != null) {
            // Create a storage reference for the image
            storageReference = storage.getReference("product_images/" + id + ".png");

            // Upload the image to Firebase Storage
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL for the uploaded image
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(downloadUri -> {
                                    // Once the image is uploaded, create the product model with the image URL
                                    Product_model productModel = new Product_model(id, title, description, price, quantity,
                                            categoryId, categoryname, downloadUri.toString(), true);

                                    firebaseFirestore.collection("products")
                                            .document(id)
                                            .set(productModel)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(AdminPannelActivity.this, "Product added successfully with image", Toast.LENGTH_SHORT).show();

                                                // Clear input fields
                                                clearFields();
                                            }).addOnFailureListener(e -> Toast.makeText(AdminPannelActivity.this,
                                                    "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                })
                                .addOnFailureListener(e -> Toast.makeText(AdminPannelActivity.this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(AdminPannelActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // If no image is selected, add the product without an image URL
            Product_model productModel = new Product_model(id, title, description, price, quantity, categoryId, categoryname, null, true);

            firebaseFirestore.collection("products")
                    .document(id)
                    .set(productModel)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AdminPannelActivity.this, "Product added successfully with image", Toast.LENGTH_SHORT).show();

                        // Clear input fields
                        clearFields();
                    }).addOnFailureListener(e -> Toast.makeText(AdminPannelActivity.this,
                            "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void clearFields() {
        titleEditText.setText("");           // Clear title
        descriptionEditText.setText("");     // Clear description
        priceEditText.setText("");           // Clear price
        quantity_id.setText("");             // Clear quantity
        categorySpinner.setSelection(0);     // Reset the category spinner to the first item
        productImageView.setImageResource(R.drawable.placeholder_image); // Set default image placeholder
        imageUri = null;                     // Reset the imageUri variable
    }

    // Function to handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            productImageView.setImageURI(imageUri);
        }
    }

}
