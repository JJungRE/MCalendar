package com.roopre.mcalendar;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class Se_Application extends Application {

    private static String TAG = "Se_Application";
    public static String Server_URL;
    public static int Font_Size;
    public static boolean Network_State;
    public static Se_LocalDbConnector Localdb;
    public static int mYear, mMonth, mDay, mWeekDay;

    public static Calendar calendar;
    public static String weekStartday;
    public static String weekEndday;
    public static Calendar startCal = null, endCal = null;

    public static String userid = "";
    public static String passwd = "";
    public static String nickname = "";
    public static String company = "";


    String[] days = {"일", "월", "화", "수", "목", "금", "토"};

    public static String getYoil(String date) {

        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)));

        //Log.d(TAG, " Calendar -> " + date.substring(0, 4));
        //Log.d(TAG, " Calendar -> " + date.substring(4, 6));
        //Log.d(TAG, " Calendar -> " + date.substring(6, 8));

        int yoil = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return days[yoil];
    }

    public static String encodingString(String str) {

        String temp = null;
        try {
            temp = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException uex) {
            Log.d(TAG, "Unsupported Exception occurred");
        }
        return temp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.Init_Value();
    }

    public void Init_Value() {


        Localdb = new Se_LocalDbConnector(this.getApplicationContext());
        Server_URL = "http://52.79.209.10/";

        Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mMonth = mMonth + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mWeekDay = c.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "year = "+mYear +", month = "+mMonth +", day = "+mDay);

        Calendar mainCal = (Calendar) c.clone();
        mainCal.set(Calendar.DAY_OF_MONTH, 1);

        int dayNum = mainCal.get(Calendar.DAY_OF_WEEK);


        calendar = Calendar.getInstance();
        startCal = (Calendar) calendar.clone();
        if (mWeekDay == 0) {
            startCal.add(Calendar.DATE, -6);
        } else {
            startCal.add(Calendar.DATE, -(mWeekDay - 1));
        }
        weekStartday = mYear + String.format("%02d", startCal.get(Calendar.MONTH) + 1) + String.format("%02d", startCal.get(Calendar.DAY_OF_MONTH));

        endCal = (Calendar) calendar.clone();

        if (mWeekDay == 0) {
        } else {
            endCal.add(Calendar.DATE, (7 - mWeekDay));
        }
        weekEndday = mYear + String.format("%02d", endCal.get(Calendar.MONTH) + 1) + String.format("%02d", endCal.get(Calendar.DAY_OF_MONTH));

        Log.d(TAG, "WeekStartDay = " + weekStartday);
        Log.d(TAG, "WeekEndDay = " + weekEndday);

    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    public static boolean strNotNull(String str) {
        if (str.equals("")) {
            return false;
        } else if (str.equals("null")) {
            return false;
        } else if (str.equals(null)) {
            return false;
        }
        return true;
    }
    public static String FullDate(Calendar c) {

        int ayear = c.get(Calendar.YEAR);
        int amonth = c.get(Calendar.MONTH) + 1;
        int aday = c.get(Calendar.DAY_OF_MONTH);

        String fulldate = String.format("%04d", ayear) + "-" + String.format("%02d", amonth) + "-" + String.format("%02d", aday);
        //Log.d(TAG, fulldate);
        //Log.d(TAG, String.format("%04d", ayear) + String.format("%02d", amonth) + String.format("%02d", aday));
        String yoil = getYoil(String.format("%04d", ayear) + String.format("%02d", amonth) + String.format("%02d", aday));

        return fulldate + "(" + yoil + "요일)";
    }
    public static String FullDate(int year, int month, int day) {

        int ayear = year;
        int amonth = month+1;
        int aday = day;

        String fulldate = String.format("%04d", ayear) + "-" + String.format("%02d", amonth) + "-" + String.format("%02d", aday);
        //Log.d(TAG, fulldate);
        //Log.d(TAG, String.format("%04d", ayear) + String.format("%02d", amonth) + String.format("%02d", aday));
        String yoil = getYoil(String.format("%04d", ayear) + String.format("%02d", amonth) + String.format("%02d", aday));

        return fulldate + " (" + yoil + "요일)";
    }
}
