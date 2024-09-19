package com.example.adminpannel.Models;



public class SoldProduct {
    private String productName;
    private  String price;
    private String quantity;
    private String userName;

    public SoldProduct() {
    }

    public SoldProduct(String productName, String price, String quantity, String userName) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public  String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUserName() {
        return userName;
    }
}
