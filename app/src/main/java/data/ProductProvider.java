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

    private static final int PETS = 100;
    private static final int PET_ID = 101;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PETS);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PET_ID);
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
            case PETS:
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PET_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        String name = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null || name.length() < 0) {
            throw new IllegalArgumentException("Product requires a name");
        }

        Integer price = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Product requires valid price");
        }

        Integer quantity = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Product requires valid quantity");
        }

        String supplier = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (supplier == null || supplier.length() < 0) {
            throw new IllegalArgumentException("Product requires a supplier");
        }

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();
        long newRowId;

        switch(uriMatcher.match(uri)) {
            case PETS:
                newRowId = sqLiteDatabase.insert(ProductEntry.TABLE_NAME, null, contentValues);

                if(newRowId == -1) {
                    Log.e("Con Provider Insert: ", "Error inserting row for " + uri);
                    return null;
                }
                break;

            default:
                throw new IllegalArgumentException("Cannot insert URI " + uri);
        }
        return ContentUris.withAppendedId(ProductEntry.CONTENT_URI, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();
        sqLiteDatabase.delete(ProductEntry.TABLE_NAME, null, null);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
