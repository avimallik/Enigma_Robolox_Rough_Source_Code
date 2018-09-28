package com.enigmarobolox.arm_avi.enigmarobolox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Arm_AVI on 3/27/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "defense_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_DEFENSE = "defense";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PHONE = "phone";

    private static final String CREATE_TABLE_DEFENSE = "CREATE TABLE "
            + TABLE_DEFENSE + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT, "+ KEY_PHONE + " TEXT );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("table", CREATE_TABLE_DEFENSE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEFENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_DEFENSE + "'");
        onCreate(db);
    }

    public long addDefenseDetail(String title, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_PHONE, phone);
        // insert row in students table
        long insert = db.insert(TABLE_DEFENSE, null, values);

        return insert;
    }

    public ArrayList<DefenseInfoModel> getAllDefense() {
        ArrayList<DefenseInfoModel> defenseInfoModelArrayList = new ArrayList<DefenseInfoModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_DEFENSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DefenseInfoModel defenseInfoModel = new DefenseInfoModel();
                defenseInfoModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                defenseInfoModel.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                defenseInfoModel.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                // adding to Students list
                defenseInfoModelArrayList.add(defenseInfoModel);
            } while (c.moveToNext());
        }
        return defenseInfoModelArrayList;
    }

    public int updateUser(int id, String title, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_PHONE, phone);
        // update row in students table base on students.is value
        return db.update(TABLE_DEFENSE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteUSer(int id) {

        // delete row in students table based on id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEFENSE, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }


}
