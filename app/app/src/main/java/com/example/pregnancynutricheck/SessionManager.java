package com.example.pregnancynutricheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String PHONE = "PHONE";
    public static final String NAME = "NAME";


    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String phone){
        editor.putBoolean(LOGIN, true);
        editor.putString(PHONE, phone);
        //editor.putString(PROFILE_PHOTO, profile_image);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        //user.put(PROFILE_PHOTO, sharedPreferences.getString(PROFILE_PHOTO, null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        ((TabActivity) context).finish();

    }

}