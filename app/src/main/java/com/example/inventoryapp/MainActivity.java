package com.example.inventoryapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import data.ProductContract.ProductEntry;
import data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        displayDbInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        displayDbInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayDbInfo() {

        String[] projection = {ProductEntry.COLUMN_PRODUCT_ID, ProductEntry.COLUMN_PRODUCT_NAME, ProductEntry.COLUMN_PRODUCT_PRICE};

        Cursor cursor = getContentResolver().query(ProductEntry.CONTENT_URI, projection, null, null);

        int idColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

        try {
            TextView displayText = findViewById(R.id.temp_display);

            while(cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                int price = cursor.getInt(priceColumnIndex);

                displayText.append("\n" + id + " - " + name + " - " + price);
            }
        }
        finally {
            cursor.close();
        }
    }
}