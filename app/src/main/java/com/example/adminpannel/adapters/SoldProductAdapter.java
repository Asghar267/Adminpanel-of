package com.example.adminpannel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminpannel.Models.SoldProduct;
import com.example.adminpannel.R;

import java.util.List;

public class SoldProductAdapter extends RecyclerView.Adapter<SoldProductAdapter.ViewHolder> {

    private List<SoldProduct> soldProducts;

    public SoldProductAdapter(List<SoldProduct> soldProducts) {
        this.soldProducts = soldProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sold_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SoldProduct soldProduct = soldProducts.get(position);
        holder.productName.setText(soldProduct.getProductName());
        holder.price.setText("Price: " +soldProduct.getPrice());
        holder.quantity.setText("Quantity: "+soldProduct.getQuantity());
        holder.userName.setText("Customer: "+soldProduct.getUserName());
    }

    @Override
    public int getItemCount() {
        return soldProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, price, quantity, userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            userName = itemView.findViewById(R.id.user_name);
        }
    }
}
