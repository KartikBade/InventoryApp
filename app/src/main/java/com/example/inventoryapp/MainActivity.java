package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import data.ProductContract.ProductEntry;
import data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

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

    private void displayDbInfo() {
        ProductDbHelper productDbHelper = new ProductDbHelper(this);
        SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + ProductEntry.TABLE_NAME, null);

        try {
            TextView displayText = findViewById(R.id.temp_display);
            displayText.setText("Number of rows: " + cursor.getCount());
        }
        finally {
            cursor.close();
        }
    }
}