
package com.example.zeynep.nativenotes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button login_btn, signup_btn;
    EditText username_edt, password_edt;
    List<String> username_list_db = new ArrayList<>();
    List<String> uname_list_db = new ArrayList<>();
    List<String> userpassword_list_db = new ArrayList<>();
    List<String> user_id_list_db = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        username_edt = findViewById(R.id.main_username_edt);
        password_edt = findViewById(R.id.main_password_edt);
        login_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        readFromDB();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login_btn :
                if (username_list_db.size() > 0) {
                    for(int i = 0; i < username_list_db.size(); i++ ) {
                        if (username_list_db.get(i).equalsIgnoreCase(username_edt.getText().toString()) &&
                                userpassword_list_db.get(i).equalsIgnoreCase(password_edt.getText().toString()) ) {
                            startActivity(new Intent(MainActivity.this, NotesActivity.class));
                            finish();
                        }else{
                            Toast.makeText(this, "" + "Invalid username or password!", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                break;
            case R.id.signup_btn :
                if (username_list_db.size() > 0) {
                    for(int i = 0; i < username_list_db.size(); i++ ) {
                        if (username_list_db.get(i).equalsIgnoreCase(username_edt.getText().toString())) {
                            Toast.makeText(this, "" + "User is exist!", Toast.LENGTH_LONG).show();
                            break;
                        }else{
                            saveToDB(username_edt.getText().toString(),username_edt.getText().toString(),password_edt.getText().toString(), "","");
                            startActivity(new Intent(MainActivity.this, NotesActivity.class));
                            finish();
                        }
                    }
                } else{
                    saveToDB(username_edt.getText().toString(),username_edt.getText().toString(),password_edt.getText().toString(), "","");
                    startActivity(new Intent(MainActivity.this, NotesActivity.class));
                    finish();
                }
               break;
        }
    }
    public void saveToDB(String username, String uname, String password, String userimg, String note_id) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.USER_NAME, username);
            cv.put(DataBaseHelper.U_NAME, uname);
            cv.put(DataBaseHelper.USER_PASSWORD, password);
            cv.put(DataBaseHelper.USER_IMG, userimg);
            cv.put(DataBaseHelper.USER_NOTE_ID, note_id);
            db.insert(DataBaseHelper.USER_TABLE_NAME, null, cv);
            /*startActivity(new Intent(MainActivity.this, NotesActivity.class));
            finish();*/

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
     /*   DataBaseHelper dataBaseHelper1 = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db1 = dataBaseHelper1.getWritableDatabase();
        String[] table_list = {"_id"};
        String selection = "_id" + " = ?";
        String[] selectionArgs = {"zeynep", "123"};
        Cursor cursor = db1.query(DataBaseHelper.USER_NAME, table_list, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int id = cursor.getInt(0);
            cursor.moveToNext();
        }
        db1.close();*/
    }

    public List<String> readFromDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        try {
            String[] stunlar = {
                    DataBaseHelper.USER_COLUMN_ID, 
                    DataBaseHelper.USER_NAME, 
                    DataBaseHelper.U_NAME,
                    DataBaseHelper.USER_PASSWORD,
                    DataBaseHelper.USER_IMG,
                    DataBaseHelper.USER_NOTE_ID};
            
            Cursor cursor = db.query(DataBaseHelper.USER_TABLE_NAME, stunlar, null, null, null, null, null);
            
            
            while (cursor.moveToNext()) {
                user_id_list_db.add(cursor.getString(0));
                username_list_db.add(cursor.getString(1));
                uname_list_db.add(cursor.getString(2));
                userpassword_list_db.add(cursor.getString(3));
            }
           /* SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("user_id", user_id);
            editor.apply();*/
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return username_list_db;
    }
}
