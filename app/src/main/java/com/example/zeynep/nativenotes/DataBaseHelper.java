package com.example.zeynep.nativenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "notesdatabase";
    public static final String NOTES_TABLE_NAME = "notestable";
    public static final String USER_TABLE_NAME = "usertable";
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
                NOTES_COLUMN_ID + " TEXT PRIMARY KEY, " +
                NOTES_USER_ID_COLUMN_NOTES + " TEXT NOT NULL, " +
                NOTES_COLUMN_NOTES + " TEXT NOT NULL )");


        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE_NAME + "("
                + USER_COLUMN_ID + " TEXT PRIMARY KEY,"
                + USER_NAME + " TEXT UNIQUE,"
                + U_NAME + " TEXT,"
                + USER_PASSWORD + " TEXT,"
                + USER_IMG + " TEXT,"
                + USER_NOTE_ID + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_ID, user.getID());
        values.put(USER_NAME, user.getUserName());
        values.put(U_NAME, user.getName());
        values.put(USER_PASSWORD, user.getPassword());

        values.put(USER_IMG, user.getImg());
        values.put(USER_NOTE_ID, user.getNote_id());

        long result = db.insert(USER_TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public long addNote(NotesModel notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_COLUMN_ID, notes.getNote_uniq_id());
        values.put(NOTES_USER_ID_COLUMN_NOTES, notes.getUser_id_from_notes());
        values.put(NOTES_COLUMN_NOTES, notes.getNote());

        long result = db.insert(NOTES_TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public UserModel getNote(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NOTES_TABLE_NAME, new String[] {
                        NOTES_COLUMN_ID,
                        NOTES_USER_ID_COLUMN_NOTES},
                NOTES_COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
            UserModel user = new UserModel();
            user.setNote_uniq_id(cursor.getInt(0));
            user.setUser_id_from_notes(cursor.getString(1));
            user.setNote(cursor.getString(2));
            return user;
        }
        else
            return null;
    }
    public UserModel getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE_NAME, new String[] {
                        USER_COLUMN_ID,
                        USER_NAME,
                        U_NAME,
                        USER_PASSWORD,
                        USER_IMG,
                        USER_NOTE_ID}, USER_COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
            UserModel user = new UserModel();
            user.setID(cursor.getString(0));
            user.setUserName(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setName(cursor.getString(3));
            user.setImg(cursor.getString(4));
            user.setNote_id(cursor.getString(5));
            return user;
        }
        else
            return null;
    }
    public List<NotesModel> getAllNotes(String userId) {
        List<NotesModel> noteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NOTES_TABLE_NAME, new String[] {
                        NOTES_COLUMN_ID,
                        NOTES_USER_ID_COLUMN_NOTES,
                        NOTES_COLUMN_NOTES},
                NOTES_USER_ID_COLUMN_NOTES + "=?",
                new String[] { String.valueOf(userId) }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                NotesModel note = new NotesModel();
                note.setNote_uniq_id(cursor.getString(0));
                note.setUser_id_from_notes(cursor.getString(1));
                note.setNote(cursor.getString(2));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
            return noteList;
    }

    public int updateNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_COLUMN_ID, note.getNote_uniq_id());
        values.put(NOTES_USER_ID_COLUMN_NOTES, note.getUser_id_from_notes());
        values.put(NOTES_COLUMN_NOTES, note.getNote());

        return db.update(NOTES_TABLE_NAME, values, NOTES_COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getNote_uniq_id())});
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<UserModel>();
        String selectQuery = "SELECT  * FROM " + USER_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel user = new UserModel();
                user.setID(cursor.getString(0));
                user.setUserName(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                user.setImg(cursor.getString(4));
                user.setNote_id(cursor.getString(5));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        return userList;
    }


    public int updateUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_ID, user.getID());
        values.put(USER_NAME, user.getUserName());
        values.put(U_NAME, user.getName());
        values.put(USER_PASSWORD, user.getPassword());

        values.put(USER_IMG, user.getImg());
        values.put(USER_NOTE_ID, user.getNote_id());

        return db.update(USER_TABLE_NAME, values, USER_COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getID())});
    }

    public void deleteNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, NOTES_COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getNote_uniq_id())});
        db.close();
    }
}

