package com.example.adminpannel.Models;

public class Product_model {

    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;
    private boolean available;
    private String categoryId;
    private String categoryname;
    private String quantity;

    public Product_model(String id, String title, String description, String price,
                         String quantity, String categoryId, String categoryname, String imageUrl, boolean available) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId; // Initialize this field
        this.imageUrl = imageUrl;
        this.available = available;
        this.categoryname = categoryname;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getDesription() {
//        return description;
//    }
//
//    public void setDesription(String desription) {
//        this.description = desription;
//    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
