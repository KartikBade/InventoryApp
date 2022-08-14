package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import data.ProductContract;
import data.ProductDbHelper;

public class DetailsActivity extends AppCompatActivity {

    private TextView addProductTbox;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;

    private ProductDbHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        addProductTbox = findViewById(R.id.add_product_tbox);
        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        supplierEditText = findViewById(R.id.supplier_edit_text);

        productDbHelper = new ProductDbHelper(this);

        addProductTbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String quantity = quantityEditText.getText().toString().trim();
                String supplier = supplierEditText.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Plastic");
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 1000);
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 10);
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, "JoshiCorp");

                SQLiteDatabase database = productDbHelper.getWritableDatabase();

                database.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

            }
        });
    }
}