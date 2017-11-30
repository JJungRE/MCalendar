package com.roopre.mcalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.roopre.mcalendar.SprefClass;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    TextView versiontv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1000);

        versiontv = (TextView) findViewById(R.id.splash_version_tv);
        versiontv.setText("ver " + getResources().getString(R.string.version));


    }

    private class splashhandler implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}