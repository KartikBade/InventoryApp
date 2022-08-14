package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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

                SQLiteDatabase database = productDbHelper.getWritableDatabase();

                String name = nameEditText.getText().toString().trim();
                String priceString = priceEditText.getText().toString().trim();
                String quantityString = quantityEditText.getText().toString().trim();
                String supplier = supplierEditText.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
                values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
                values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplier);

                database.insert(ProductEntry.TABLE_NAME, null, values);
            }
        });
    }
}