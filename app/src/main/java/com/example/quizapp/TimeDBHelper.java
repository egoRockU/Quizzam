package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TimeDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Time.db";
    public static final String TABLE_NAME = "TimeTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME_IN_SEC = "timeInSec";
    public static final String COLUMN_TIME_IN_MILLIS = "timeInMillis";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TIME_IN_SEC + " INTEGER, "
            + COLUMN_TIME_IN_MILLIS + " INTEGER);";

    public TimeDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void add(int timeInSec,int timeInMillis){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TIME_IN_SEC, timeInSec);
        cv.put(COLUMN_TIME_IN_MILLIS, timeInMillis);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            System.out.println("Insert Record Failed");
        } else {
            System.out.println("Record Inserted");
        }
    }

    void update(String COLUMN_ID, int timeInSec, int timeInMillis){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TIME_IN_SEC, timeInSec);
        cv.put(COLUMN_TIME_IN_MILLIS, timeInMillis);
        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{COLUMN_ID});
        if(result == -1){
            System.out.println("Time Update Failed");
        } else {
            System.out.println("Time Stats Update Successful");
        }

    }

    Cursor readData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = null;
        if(db != null){
            data = db.rawQuery(query, null);
        }
        return data;
    }
}
