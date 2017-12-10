package com.roopre.mcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    TextView versiontv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        /*Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1000);
*/
        /*versiontv = (TextView) findViewById(R.id.splash_version_tv);
        PackageInfo info = null;
        String versionName = "";
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versiontv.setText("ver " + versionName);
*/
    }

    private class splashhandler implements Runnable {
            @Override
            public void run() {
            Log.d(TAG, "splashhandler");
            if (Se_Application.strNotNull(Se_Application.Localdb.get_dataS("token"))) {
                if (Se_Application.strNotNull(Se_Application.Localdb.get_dataS("userid"))) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "token -> not null");
                    String server_url = "signup.php";
                    HashMap<String, String> send_arg = new HashMap<String, String>();
                    send_arg.put("token", Se_Application.Localdb.get_dataS("token"));
                    Server_con serverCon = new Server_con(server_url, send_arg);
                    String result = serverCon.Receive_Server();
                    Log.d(TAG, result);

                    if (result.equals("duplicate id")) {
                        Handler hd = new Handler();
                        hd.postDelayed(new splashhandler(), 1000);
                    } else {
                        Se_Application.Localdb.set_dataS("userid", result);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } else {
                MyFirebaseInstanceIDService service = new MyFirebaseInstanceIDService();
                service.onTokenRefresh();
                Log.d(TAG, "token -> null");
                Handler hd = new Handler();
                hd.postDelayed(new splashhandler(), 1000);
            }
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