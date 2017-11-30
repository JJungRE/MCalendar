package com.roopre.mcalendar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2016-12-20.
 */

public class Se_LocalDbConnector {

    private String db_name = "REF_CALENDAR";
    SharedPreferences Localdb = null;


    public Se_LocalDbConnector(Context context){
        Localdb = context.getSharedPreferences(db_name, 0);
    }

    public String get_dataS(String Fn){
        return Localdb.getString(Fn, "");
    }
    public boolean get_dataB(String Fn){
        return Localdb.getBoolean(Fn, false);
    }
    public int get_dataI(String Fn){
        return Localdb.getInt(Fn, -1);
    }

    public void set_dataS(String Fn, String val){
        SharedPreferences.Editor se = Localdb.edit();
        se.putString(Fn,val);
        se.commit();
    }
    public void set_dataB(String Fn, boolean val){
        SharedPreferences.Editor se = Localdb.edit();
        se.putBoolean(Fn,val);
        se.commit();
    }
    public void set_dataI(String Fn, int val){
        SharedPreferences.Editor se = Localdb.edit();
        se.putInt(Fn,val);
        se.commit();
    }
}
