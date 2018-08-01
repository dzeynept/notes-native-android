
package com.example.zeynep.nativenotes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    List<UserModel> userModels = new ArrayList<>();
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserModel userModel = UserPreferences.getInstance(this).readUser();
        if (userModel != null) {
            startActivity(new Intent(this, NotesActivity.class));
            finish();
        }
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        username_edt = findViewById(R.id.main_username_edt);
        password_edt = findViewById(R.id.main_password_edt);
        login_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        readFromDB();
        readUsersFromDB();

    }

    private void readUsersFromDB() {

        userModels = dataBaseHelper.getAllUsers();
    }

    private UserModel checkLog(String username, String password){
        if (userModels != null){
            for (UserModel u : userModels) {
                if (u.getUserName().equalsIgnoreCase(username) &&
                        u.getPassword().equalsIgnoreCase(password)){
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login_btn :

                UserModel userModel = checkLog(username_edt.getText().toString(), password_edt.getText().toString());
                if (userModel != null && !username_edt.getText().toString().equalsIgnoreCase("")
                        &&!password_edt.getText().toString().equalsIgnoreCase("")){
                    UserPreferences.getInstance(this).saveUser(userModel);
                    startActivity(new Intent(this, NotesActivity.class));
                    finish();
                } else if (userModel != null &&username_edt.getText().toString().equalsIgnoreCase("") ) {
                    Toast.makeText(this, "User name is empty!", Toast.LENGTH_SHORT).show();

                }
                else if (userModel != null && password_edt.getText().toString().equalsIgnoreCase("") ) {
                    Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.signup_btn :
                UserModel userModel2 = checkLog(username_edt.getText().toString(), password_edt.getText().toString());

                if (userModel2 == null && !username_edt.getText().toString().equalsIgnoreCase("")
                        &&!password_edt.getText().toString().equalsIgnoreCase("") ){
                    userModel2 = new UserModel(username_edt.getText().toString(), password_edt.getText().toString());

                    UserPreferences.getInstance(this).saveUser(userModel2);
                    dataBaseHelper.addUser(userModel2);
                    startActivity(new Intent(this, NotesActivity.class));
                    finish();
                }else if (userModel2 == null && username_edt.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(this, "User name is empty!", Toast.LENGTH_SHORT).show();
                }
                else if (userModel2 == null && password_edt.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "User exists!", Toast.LENGTH_SHORT).show();


                break;
        }
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

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return username_list_db;
    }
    @Override
    public void onBackPressed() {
    }
}
