package com.roopre.mcalendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by munjongmin on 2017. 9. 11..
 */

public class SprefClass {

    SharedPreferences spref = null;
    private String spref_name = "LocalSpref";

    public SprefClass(Context context){
        spref = context.getSharedPreferences(spref_name, 0);
    }

    public String GetSprefString(String key){
        return spref.getString(key, null);
    }
    public Boolean getSprefBool(String key){
        return spref.getBoolean(key, false);
    }

    public void setSprefString(String key, String value){
        SharedPreferences.Editor editor = spref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setSprefBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = spref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
