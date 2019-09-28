package com.faizikhwan.recipeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    //log
    private static String TAG = "DatabaseHelper";

    //Database name
    private static final String DATABASE_NAME = "Recipe.db";

    //Table names
    private static final String TABLE_NAME_RECIPE = "RECIPE";

    //Table Recipe
    private static final String COL_1_RECIPE = "ID";
    private static final String COL_2_RECIPE = "TITLE";
    private static final String COL_3_RECIPE = "INGREDIENT";
    private static final String COL_4_RECIPE = "STEP";
    private static final String COL_5_RECIPE = "TYPE";
    private static final String COL_6_RECIPE = "IMAGE";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Create tables when first time open the apps.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "onCreate");

        //creating required tables
        db.execSQL("create table " + TABLE_NAME_RECIPE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, INGREDIENT TEXT, STEP TEXT, TYPE TEXT, IMAGE BLOB);");
    }

    /**
     * Upgrade tables.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_RECIPE);

        //create new tables
        onCreate(db);
    }

    /**
     * Table TABLE_NAME_RECIPE
     */
    public boolean insertDataRecipe(String title, String ingredient, String step, String type, byte[] image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_RECIPE, title);
        contentValues.put(COL_3_RECIPE, ingredient);
        contentValues.put(COL_4_RECIPE, step);
        contentValues.put(COL_5_RECIPE, type);
        contentValues.put(COL_6_RECIPE, image);
        long res = db.insert(TABLE_NAME_RECIPE, null, contentValues);
        if(res == -1)
            return false;
        else
            return true;
    }

    /**
     * Table TABLE_NAME_RECIPE
     */
    public boolean updateDataRecipe(String id, String title, String ingredient, String step, String type, byte[] image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_RECIPE, title);
        contentValues.put(COL_3_RECIPE, ingredient);
        contentValues.put(COL_4_RECIPE, step);
        contentValues.put(COL_5_RECIPE, type);
        contentValues.put(COL_6_RECIPE, image);
        db.update(TABLE_NAME_RECIPE, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    /**
     * Table TABLE_NAME_RECIPE.
     */
    public Cursor getDataFromRecipe()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_NAME_RECIPE;
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    /**
     * Table TABLE_NAME_RECIPE.
     */
    public Cursor getDataFromRecipeWithID(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_NAME_RECIPE + " where ID = '" + id + "';";
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    /**
     * Table TABLE_NAME_RECIPE.
     */
    public Cursor getDataFromRecipeWithType(String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_NAME_RECIPE + " where TYPE = '" + type + "';";
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    /**
     * Table TABLE_NAME_RECIPE.
     */
    public void deleteDataFromRecipe(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_RECIPE, COL_1_RECIPE + "=" + id, null);
        db.close();
    }

}
