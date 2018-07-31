package com.example.zeynep.nativenotes;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "notesdatabase";
    public static final String NOTES_TABLE_NAME = "notes_table";
    public static final String USER_TABLE_NAME = "user_table";
    public static final String NOTES_COLUMN_ID = "_id";
    public static final String USER_COLUMN_ID = "_id";
    public static final String NOTES_COLUMN_NOTES = "note";
    public static final String NOTES_USER_ID_COLUMN_NOTES = "user_id";

    public static final String USER_NAME = "username";
    public static final String U_NAME = "name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_IMG = "img";
    public static final String USER_NOTE_ID = "note_id";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + NOTES_TABLE_NAME + "(" +
                NOTES_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                NOTES_USER_ID_COLUMN_NOTES + " TEXT NOT NULL," +
                NOTES_COLUMN_NOTES + " TEXT NOT NULL)");

        sqLiteDatabase.execSQL(" CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                USER_NAME + " TEXT NOT NULL, " +
                U_NAME + " TEXT NOT NULL," +
                USER_PASSWORD + " TEXT NOT NULL," +
                USER_IMG + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
