package com.example.dawdtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dawdtest.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Employees_Manager";
    // Table name: Note.
    private static final String TABLE_NOTE = "Employee";

    private static final String COLUMN_PRODUCT_ID ="id";
    private static final String COLUMN_PRODUCT_NAME ="name";
    private static final String COLUMN_PRODUCT_QUANTITY = "quantity";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String script = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCT_NAME + " TEXT,"
               + COLUMN_PRODUCT_QUANTITY +" INTEGER" + ")";
        // Execute script.
        db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
// Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);


        // Recreate
        onCreate(db);
    }
    // If Note table has no data
    // default, Insert 2 records.
    public void createDefaultEmployeesIfNeed()  {
        int count = this.getNotesCount();
        if(count ==0 ) {
            Product note1 = new Product("Iphone 11 pro max"
                  ,20);
            Product note2 = new Product("SamSung galaxy s8",
                    22);

            this.addProduct(note1);
            this.addProduct(note2);
        }
    }
    public void addProduct(Product product) {
        Log.i(TAG, "MyDatabaseHelper.addEmployee ... " + product.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_QUANTITY ,product.getQuantity());

        // Inserting Row
        db.insert(TABLE_NOTE, null, values);

        // Closing database connection
        db.close();
    }
    public Product getProduct(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[] { COLUMN_PRODUCT_ID,
                        COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_QUANTITY }, COLUMN_PRODUCT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),cursor.getInt(2));
        // return note
        return product;
    }


    public List<Product> getAllEmployees() {
        Log.i(TAG, "MyDatabaseHelper.getAllEmployees ... " );

        List<Product> employeeList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setQuantity(cursor.getInt(2));
                // Adding note to list
                employeeList.add(product);
            } while (cursor.moveToNext());
        }

        // return note list
        return employeeList;
    }

    public int getNotesCount() {
        Log.i(TAG, "MyDatabaseHelper.getEmployee ... " );

        String countQuery = "SELECT  * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public int updateNote(Product product) {
        Log.i(TAG, "MyDatabaseHelper.updateEmployee ... "  + product.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_QUANTITY, product.getQuantity());


        // updating row
        return db.update(TABLE_NOTE, values, COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteNote(Product product) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + product.getName() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COLUMN_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
        db.close();
    }
}
