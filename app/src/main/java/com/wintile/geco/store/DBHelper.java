package com.wintile.geco.store;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wintile.geco.models.ProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
/**
 * Created by Sharath Kumar on 16/9/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GecoDB";

    // Table Names00
    private static final String TABLE_PRODUCTLIST = "product_list_table";
    private static final String TABLE_SAVED = "saved_product_table";


    // GunHolder Table - column names
    private static final String KEY_PRODUCT_ID = "ProductID";
    private static final String KEY_PRODUCT_NAME = "ProductName";
    private static final String KEY_IMAGE_URL = "ImageUrl";
    private static final String KEY_CATEGORY = "Category";
    private static final String KEY_DESC = "Description";
    private static final String KEY_SPECS = "Specification";
    private static final String KEY_SHARE_URL = "ShareUrl";

    private static final String CREATE_TABLE_PRODUCT_LIST = "CREATE TABLE " + TABLE_PRODUCTLIST + " ("
            + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY, "
            + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_IMAGE_URL + " TEXT, "
            + KEY_DESC + " TEXT, "
            + KEY_SPECS + " TEXT, "
            + KEY_SHARE_URL + " TEXT, "
            + KEY_CATEGORY + " TEXT )";

    private static final String CREATE_TABLE_SAVED = "CREATE TABLE " + TABLE_SAVED + " ("
            + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY, "
            + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_IMAGE_URL + " TEXT, "
            + KEY_DESC + " TEXT, "
            + KEY_SPECS + " TEXT, "
            + KEY_SHARE_URL + " TEXT, "
            + KEY_CATEGORY + " TEXT )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        Log.d("Create", CREATE_TABLE_PRODUCT_LIST);
        db.execSQL(CREATE_TABLE_PRODUCT_LIST);
        db.execSQL(CREATE_TABLE_SAVED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED);
        // create new tables
        onCreate(db);
    }

    // ------------------------ "todos" table methods ----------------//

    /*
	 * Creating a productList.
	 */
    public void createTableProductList(ProductModel products) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, products.getProdutID());
        values.put(KEY_PRODUCT_NAME, products.getProductName());
        values.put(KEY_IMAGE_URL, products.getImgUrl());
        values.put(KEY_CATEGORY, products.getCategory());
        values.put(KEY_SHARE_URL, products.getShareUrl());
        values.put(KEY_SPECS, products.getSpecs());
        values.put(KEY_DESC, products.getDesc() );

        // insert row
        db.insert(TABLE_PRODUCTLIST, null, values);
    }

    /*
     * Creating a productList.
     */
    public void CreateTableSaved(ProductModel products) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, products.getProdutID());
        values.put(KEY_PRODUCT_NAME, products.getProductName());
        values.put(KEY_IMAGE_URL, products.getImgUrl());
        values.put(KEY_CATEGORY, products.getCategory());
        values.put(KEY_SHARE_URL, products.getShareUrl());
        values.put(KEY_SPECS, products.getSpecs());
        values.put(KEY_DESC, products.getDesc() );
        // insert row
        db.insert(TABLE_SAVED, null, values);
    }


    public List<ProductModel> getAllProducts() {
        List<ProductModel> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTLIST;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProductModel td = new ProductModel();
                Log.e("allprod", (c.getString(c.getColumnIndex(KEY_PRODUCT_NAME))));
                td.setProdutID(c.getInt((c.getColumnIndex(KEY_PRODUCT_ID))));
                td.setProductName((c.getString(c.getColumnIndex(KEY_PRODUCT_NAME))));
                td.setImgUrl(c.getString(c.getColumnIndex(KEY_IMAGE_URL)));
                td.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
                td.setShareUrl(c.getString(c.getColumnIndex(KEY_SHARE_URL)));
                td.setDesc(c.getString(c.getColumnIndex(KEY_DESC)));
                td.setSpecs(c.getString(c.getColumnIndex(KEY_SPECS)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public List<ProductModel> getSaved() {
        List<ProductModel> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SAVED;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProductModel td = new ProductModel();
                Log.e("allprod", (c.getString(c.getColumnIndex(KEY_PRODUCT_NAME))));
                td.setProdutID(c.getInt((c.getColumnIndex(KEY_PRODUCT_ID))));
                td.setProductName((c.getString(c.getColumnIndex(KEY_PRODUCT_NAME))));
                td.setImgUrl(c.getString(c.getColumnIndex(KEY_IMAGE_URL)));
                td.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
                td.setShareUrl(c.getString(c.getColumnIndex(KEY_SHARE_URL)));
                td.setDesc(c.getString(c.getColumnIndex(KEY_DESC)));
                td.setSpecs(c.getString(c.getColumnIndex(KEY_SPECS)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public List<String> getCategory(){
        String selectQuery = "SELECT "+ KEY_CATEGORY + " FROM " + TABLE_PRODUCTLIST + " GROUP BY " + KEY_CATEGORY;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<String> category = new ArrayList<String>();

        // looping through all rows and adding to list
        try {
            if (c.moveToFirst()) {
                do {
                  //  Log.e("data", c.getString(c.getColumnIndex(KEY_CATEGORY)));
                    category.add(c.getString(c.getColumnIndex(KEY_CATEGORY)));

                } while (c.moveToNext());
            }
            Log.e("length", String.valueOf(category.size()));
        }catch(Exception e){}
        return category;
    }
    public List<ProductModel> getProducts(String catagory) {
        List<ProductModel> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTLIST + " WHERE " + KEY_CATEGORY + " = '" + catagory + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProductModel td = new ProductModel();
                //Log.e("allprod", (c.getString(c.getColumnIndex(KEY_PRODUCT_NAME))));
                td.setProdutID(c.getInt((c.getColumnIndex(KEY_PRODUCT_ID))));
                td.setProductName((c.getString(c.getColumnIndex(KEY_PRODUCT_NAME))));
                td.setImgUrl(c.getString(c.getColumnIndex(KEY_IMAGE_URL)));
                td.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
                td.setShareUrl(c.getString(c.getColumnIndex(KEY_SHARE_URL)));
                td.setDesc(c.getString(c.getColumnIndex(KEY_DESC)));
                td.setSpecs(c.getString(c.getColumnIndex(KEY_SPECS)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public void deleteRecord(int productID) {


        SQLiteDatabase db = this.getReadableDatabase();
         db.delete(TABLE_SAVED, KEY_PRODUCT_ID + " = " + productID, null );
    }
    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTLIST,null,null);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}