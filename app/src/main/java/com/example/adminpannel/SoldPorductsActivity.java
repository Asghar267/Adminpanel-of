package com.example.adminpannel;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.adminpannel.Models.SoldProduct;
import com.example.adminpannel.adapters.SoldProductAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SoldPorductsActivity extends AppCompatActivity {

        private RecyclerView soldProductsRecyclerView;
        private SoldProductAdapter soldProductAdapter;
        private List<SoldProduct> soldProducts;
        private FirebaseFirestore firestore;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sold_porducts);

            firestore = FirebaseFirestore.getInstance();
            soldProductsRecyclerView = findViewById(R.id.sold_products_recycler_view);

            // Initialize RecyclerView
            soldProducts = new ArrayList<>();
            soldProductAdapter = new SoldProductAdapter(soldProducts);
            soldProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            soldProductsRecyclerView.setAdapter(soldProductAdapter);

            // Fetch sold products from Firestore
            fetchSoldProducts();
        }

        private void fetchSoldProducts() {
            CollectionReference soldProductsRef = firestore.collection("Sold Products");

            soldProductsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    soldProducts.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String productName = document.getString("productName");
                        String price = document.getString("price");
                        String quantity = document.getString("quantity");
                        String userId = document.getString("userId");

                        // Fetch user name based on userId
                        fetchUserName(userId, productName, price, quantity);
                    }
                } else {
                    Toast.makeText(SoldPorductsActivity. this, "Failed to fetch data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void fetchUserName(String userId, String productName, String price,  String quantity) {
            firestore.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getString("name");
                    SoldProduct soldProduct = new SoldProduct(productName, price, quantity, userName);
                    soldProducts.add(soldProduct);
                    soldProductAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SoldPorductsActivity.this, "Failed to fetch user name: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
