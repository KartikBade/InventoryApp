package com.example.inventoryapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.ProductContract.ProductEntry;
import data.ProductDbHelper;

public class DetailsActivity extends AppCompatActivity {

    private TextView addProductTbox;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        addProductTbox = findViewById(R.id.add_product_tbox);
        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        supplierEditText = findViewById(R.id.supplier_edit_text);

        addProductTbox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {

                String name = nameEditText.getText().toString().trim();
                String priceString = priceEditText.getText().toString().trim();
                String quantityString = quantityEditText.getText().toString().trim();
                String supplier = supplierEditText.getText().toString().trim();

                int price = -1;
                int quantity = -1;
                try {
                    price = Integer.parseInt(priceString);
                    quantity = Integer.parseInt(quantityString);
                } catch (Exception e) {
                    Log.e("DetailsActivity: ", "Error converting price and/or quantity from string to int" + e);
                }

                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
                values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplier);

                Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

                if(uri == null) {
                    Toast.makeText(getApplicationContext(), "Failed to insert Product", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Product inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}