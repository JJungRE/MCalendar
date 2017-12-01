package com.roopre.mcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class UsedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "UsedActivity";
    EditText used_et;
    TextView ex_used_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.used);

        Button used_close_bt = (Button) findViewById(R.id.used_close_bt);
        used_close_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsedActivity.this.finish();
            }
        });

        used_et = (EditText) findViewById(R.id.used_et);


        // 사용처리 버튼
        Button used_set_bt = (Button) findViewById(R.id.used_set_bt);
        used_set_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String seq = intent.getExtras().getString("schedule_seq");
                HashMap<String, String> send_arg = new HashMap<String, String>();
                String server_url = "insert_used.php";
                send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
                send_arg.put("pay", used_et.getText().toString());
                send_arg.put("schedule_seq", seq);
                send_arg.put("used_point", "500");
                Server_con serverCon = new Server_con(server_url, send_arg);
                String result = serverCon.Receive_Server();
                Log.d(TAG, result+","+Se_Application.Localdb.get_dataS("userid")+","+used_et.getText().toString()+","+Se_Application.Localdb.get_dataS("schedule_seq"));

                UsedActivity.this.finish();
                Toast.makeText(UsedActivity.this,"seq value: "+seq + "저장",Toast.LENGTH_SHORT).show();

            }


        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_information) {
            // Handle the camera action
        } else if (id == R.id.nav_notice) {

        } else if (id == R.id.nav_usage) {

        } else if (id == R.id.nav_score) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
