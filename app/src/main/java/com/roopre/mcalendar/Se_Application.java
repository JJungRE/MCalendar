package com.roopre.mcalendar;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.victor.loading.newton.NewtonCradleLoading;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class Se_Application extends Application {

    public static String preActivity = "";
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

    public static  AlphaAnimation animation1, animation2;
    public static String userid = "";
    public static String passwd = "";
    public static String nickname = "";
    public static String company = "";

    public static  long backKeyPressedTime = 0;
    public static RelativeLayout loading_layout;
    public static NewtonCradleLoading loading_bar;

    public static int selectedMonth = 0;
    public static int selectedYear = 0;
    public static String[] days = {"일", "월", "화", "수", "목", "금", "토"};
    public static String[] daysEng = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    private static class splashhandler implements Runnable {
        boolean val = false;
        public splashhandler(boolean val) {
            this.val = val;
        }

        @Override
        public void run() {
            if (val) {
                loading_layout.setVisibility(View.VISIBLE);
                loading_bar.start();
            } else {
                loading_bar.stop();
                loading_layout.setVisibility(View.GONE);
            }
        }
    }

    public static void isLoading(boolean val) {
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(val), 10);
    }

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

        animation1 = new AlphaAnimation(1.0F, 0.2F);
        animation1.setDuration(300);
        animation2 = new AlphaAnimation(0.2F, 1.0F);
        animation2.setDuration(300);

        Localdb = new Se_LocalDbConnector(this.getApplicationContext());
        Server_URL = "http://13.124.29.54/";

        Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mMonth = mMonth + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mWeekDay = c.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "year = " + mYear + ", month = " + mMonth + ", day = " + mDay);

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

    public static String FullDateNotYoil(Calendar c) {

        int ayear = c.get(Calendar.YEAR);
        int amonth = c.get(Calendar.MONTH) + 1;
        int aday = c.get(Calendar.DAY_OF_MONTH);

        String fulldate = String.format("%04d", ayear) + "-" + String.format("%02d", amonth) + "-" + String.format("%02d", aday);
        return fulldate;
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
        int amonth = month + 1;
        int aday = day;

        String fulldate = String.format("%04d", ayear) + "-" + String.format("%02d", amonth) + "-" + String.format("%02d", aday);
        //Log.d(TAG, fulldate);
        //Log.d(TAG, String.format("%04d", ayear) + String.format("%02d", amonth) + String.format("%02d", aday));
        String yoil = getYoil(String.format("%04d", ayear) + String.format("%02d", amonth) + String.format("%02d", aday));

        return fulldate + " (" + yoil + "요일)";
    }
}
