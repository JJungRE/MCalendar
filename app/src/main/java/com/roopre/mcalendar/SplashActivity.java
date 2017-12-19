package com.roopre.mcalendar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    TextView versiontv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        versiontv = (TextView) findViewById(R.id.splash_version_tv);
        PackageInfo info = null;
        String versionName = "";
        String latestVersion = "";
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = info.versionName;
            VersionChecker versionChecker = new VersionChecker();
            latestVersion = versionChecker.execute().get();

            Log.d(TAG, latestVersion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        versiontv.setText("ver " + versionName);
        if (versionName.equals(latestVersion)) {
            Handler hd = new Handler();
            hd.postDelayed(new splashhandler(), 1000);
        } else {
            Toast.makeText(this, "최신 버전은 " + latestVersion + "입니다.\n정상적인 이용을 위해 업데이트를 부탁드립니다", Toast.LENGTH_LONG).show();
            Handler hd = new Handler();
            hd.postDelayed(new splashhandler(), 1000);
            /*final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }*/
        }


    }

    public class VersionChecker extends AsyncTask<String, String, String> {
        String newVersion;
        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.roopre.mcalendar")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }
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
                        Se_Application.Localdb.set_dataB("push",true);
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