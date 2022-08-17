package com.example.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import data.ProductContract.ProductEntry;

public class ProductAdapter extends CursorAdapter {

    public ProductAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.item_name);
        TextView priceTextView = view.findViewById(R.id.item_price);
        TextView quantityTextView = view.findViewById(R.id.item_quantity);
        TextView saleButton = view.findViewById(R.id.sale);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));

        nameTextView.setText(name);
        priceTextView.setText("Rs." + price);
        quantityTextView.setText(String.valueOf(quantity));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}