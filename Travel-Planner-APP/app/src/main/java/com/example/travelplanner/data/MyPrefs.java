package com.example.travelplanner.data;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefs  {
    private static final String PREF_NAME = "MyPrefs";

    public static boolean getIntroCompletedStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("intro_completed", false);
    }
    public static void setIntroCompletedStatus(Context context, boolean completed) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("intro_completed", completed);
        editor.apply();
    }
    public static void setSignUpCompletedStatus(Context context, boolean completed) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("signup_completed", completed);
        editor.apply();
    }
    public static boolean getSignUpCompletedStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("signup_completed", false);
    }
    public static void setToken(Context context,String token){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("jwt", token);
        editor.apply();
    }
    public static void setData(Context context,String data){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data", data);
        editor.apply();
    }
    public static String getData(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("data","");
    }
    public static String getToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("jwt","");
    }
    public static void removeToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwt");
        editor.apply();
    }
}
