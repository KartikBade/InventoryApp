package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.ProductContract.ProductEntry;
import data.ProductDbHelper;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView addProductTbox;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;
    private Uri currentUri;
    private static final int EXISTING_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        addProductTbox = findViewById(R.id.add_product_tbox);
        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        supplierEditText = findViewById(R.id.supplier_edit_text);

        Intent intent = getIntent();
        currentUri = intent.getData();

        if(currentUri != null) {
            setTitle("Edit Product");
            addProductTbox.setText("Save Changes");
            getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);
        }
        else {
            setTitle("Enter Product");
        }

        addProductTbox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        String name = nameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String supplier = supplierEditText.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(priceString) || TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplier)) {
            Toast.makeText(this, "Enter relevant information", Toast.LENGTH_SHORT).show();
        }

        int price = -1;
        int quantity = -1;
        try {
            price = Integer.parseInt(priceString);
            quantity = Integer.parseInt(quantityString);
        } catch (Exception e) {
            Toast.makeText(this, "Enter relevant information", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplier);

        if(currentUri != null) {
            int rowsUpdated = getContentResolver().update(currentUri, values, null, null);
            if(rowsUpdated == 0) {
                Toast.makeText(getApplicationContext(), "Failed to update Product", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if(uri == null) {
                Toast.makeText(getApplicationContext(), "Failed to insert Product", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Product inserted successfully", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER
        };

        return new CursorLoader(this, currentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(data.moveToNext()) {

            int nameIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER);

            nameEditText.setText(data.getString(nameIndex));
            priceEditText.setText(Integer.toString(data.getInt(priceIndex)));
            quantityEditText.setText(Integer.toString(data.getInt(quantityIndex)));
            supplierEditText.setText(data.getString(supplierIndex));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        nameEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        supplierEditText.setText("");
    }
}