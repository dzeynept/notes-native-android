package com.example.zeynep.nativenotes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class UserPreferences {

    private static UserPreferences instance;
    private SharedPreferences sharedPreferences;
    private String userKey;

    private UserPreferences(Context context) {
        userKey = "user";
        sharedPreferences = context.getApplicationContext().getSharedPreferences(userKey, Context
                .MODE_PRIVATE);
    }

    public static UserPreferences getInstance(Context context) {

        if (instance == null)
            instance = new UserPreferences(context);

        return instance;
    }

    public void saveUser(UserModel userModel) {//id,token,phone,confirmcode,interest vb.

        try {
            Gson gson = new Gson();
            String stringUser = gson.toJson(userModel);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("logged_user", stringUser);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserModel readUser() {

        try {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("logged_user", null);
            return gson.fromJson(json, UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserModel removeUser() {

        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("logged_user");
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}