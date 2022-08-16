package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import data.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider {

    private ProductDbHelper productDbHelper;

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {

        productDbHelper = new ProductDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();
        Cursor cursor;

        switch(uriMatcher.match(uri)) {
            case PRODUCTS:
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri)) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;

            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalStateException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri newRowUri;
        switch(uriMatcher.match(uri)) {
            case PRODUCTS:
                newRowUri = insertProduct(uri, contentValues);
                break;

            default:
                throw new IllegalArgumentException("Cannot insert URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return newRowUri;
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {

        String name = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null || name.length() < 0) {
            return null;
        }

        Integer price = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price == null || price < 0) {
            return null;
        }

        Integer quantity = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity == null || quantity < 0) {
            return null;
        }

        String supplier = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        if (supplier == null || supplier.length() < 0) {
            return null;
        }

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();
        long newRowId = sqLiteDatabase.insert(ProductEntry.TABLE_NAME, null, contentValues);

        if(newRowId == -1) {
            Log.e("Con Provider Insert: ", "Error inserting row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(ProductEntry.CONTENT_URI, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();
        int newRowId;
        switch (uriMatcher.match(uri)) {
            case PRODUCT_ID:
                s = ProductEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                newRowId = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, s, strings);
                break;

            default:
                throw new IllegalArgumentException("Error deleting row for: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return newRowId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        int newRowId;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                newRowId = updateProduct(uri, contentValues, s, strings);
                break;

            case PRODUCT_ID:
                s = ProductEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                newRowId = updateProduct(uri, contentValues, s, strings);
                break;

            default:
                throw new IllegalArgumentException("Cannot update URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return newRowId;
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if(contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null || name.length() < 0) {
                return 0;
            }
        }

        if(contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price == null || price < 0) {
                return 0;
            }
        }

        if(contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null || quantity < 0) {
                return 0;
            }
        }

        if(contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER)) {
            String supplier = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            if (supplier == null || supplier.length() < 0) {
                return 0;
            }
        }

        if(contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();
        return sqLiteDatabase.update(ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }
}
