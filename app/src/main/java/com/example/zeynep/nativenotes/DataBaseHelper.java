package com.example.zeynep.nativenotes;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "notesdatabase";
    public static final String NOTES_TABLE_NAME = "notestable";
    public static final String NOTES_COLUMN_ID = "_id";
    public static final String NOTES_COLUMN_NOTES = "note";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

/*    public DataBaseHelper() {
        super();
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + NOTES_TABLE_NAME + "(" +
                NOTES_COLUMN_ID + " TEXT PRIMARY KEY, " +
                NOTES_COLUMN_NOTES + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
