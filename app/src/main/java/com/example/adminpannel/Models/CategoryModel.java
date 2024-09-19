package com.example.adminpannel.Models;

//public class CategoryModel {
//    private String name;

    public class CategoryModel {
        private String id;
        private String name;


        // No-argument constructor required by Firestore
        public CategoryModel() {
        }
        public CategoryModel(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name; // This is what will be displayed in the Spinner
        }
    }
