package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserStats.db";
    private static final String TABLE_NAME = "user_stats";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HIGHSCORE = "high_score";
    private static final String COLUMN_QUIZCOMPLETED = "quiz_completed";
    private static final String COLUMN_PERFECTQUIZ = "perfect_quiz";


    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HIGHSCORE + " INTEGER, " +
                COLUMN_QUIZCOMPLETED + " INTEGER, " +
                COLUMN_PERFECTQUIZ + " INTEGER);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void add(int highScore, int quizCompleted, int perfectQuiz){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HIGHSCORE, highScore);
        cv.put(COLUMN_QUIZCOMPLETED, quizCompleted);
        cv.put(COLUMN_PERFECTQUIZ, perfectQuiz);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            System.out.println("User Stats Insert Record Failed");
        } else {
            System.out.println("User Stats Record Inserted");
        }
        db.close();
    }

    void update(String COLUMN_ID, int highScore, int quizCompleted, int perfectQuiz){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HIGHSCORE, highScore);
        cv.put(COLUMN_QUIZCOMPLETED, quizCompleted);
        cv.put(COLUMN_PERFECTQUIZ, perfectQuiz);
        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{COLUMN_ID});

        if(result == -1){
            System.out.println("User Stats Update Failed");
        } else {
            System.out.println("User Stats Update Successful");
        }
        db.close();
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
