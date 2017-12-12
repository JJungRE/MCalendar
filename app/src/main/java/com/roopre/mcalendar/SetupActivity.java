package com.roopre.mcalendar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private String TAG = "SetupActivity";
    RadioButton pushOn, pushOff;
    Menu menu;

    String category = "";
    GridView gridView;
    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> selectedList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        final ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
        textviewTitle.setText("설정");
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.color.transparent);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_red)));

        pushOn = (RadioButton) findViewById(R.id.pushOn);
        pushOff = (RadioButton) findViewById(R.id.pushOff);
        pushOn.setOnCheckedChangeListener(this);
        if (Se_Application.Localdb.get_dataB("push")) {
            pushOn.setChecked(true);
            pushOn.setTextColor(getResources().getColor(R.color.White));
            pushOff.setTextColor(getResources().getColor(R.color.main_gray));
        } else {
            pushOff.setChecked(true);
            pushOn.setTextColor(getResources().getColor(R.color.main_gray));
            pushOff.setTextColor(getResources().getColor(R.color.White));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //SetInit();
    }

    private void SetInit() {

        String server_url = "load_user_pick.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);
        if (Se_Application.strNotNull(result)) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                selectedList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.getString("category").equals("all")) {
                        selectedList.add(jsonObject.getString("category"));
                    } else {
                        selectedList.add(jsonObject.getString("category"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        server_url = "get_category.php";
        send_arg = new HashMap<String, String>();
        send_arg.put("company", Se_Application.Localdb.get_dataS("company"));
        serverCon = new Server_con(server_url, send_arg);
        result = serverCon.Receive_Server();
        Log.d(TAG, result);

        if (Se_Application.strNotNull(result)) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                categoryList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    categoryList.add(jsonObject.getString("category"));
                }


                SetupCategoryAdapter setupCategoryAdapter = new SetupCategoryAdapter(this, categoryList, selectedList);
                gridView.setAdapter(setupCategoryAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {
                        category = categoryList.get(position);
                        Log.d(TAG, "Click => " + categoryList.get(position));
                        String server_url = "set_category.php";
                        HashMap<String, String> send_arg = new HashMap<String, String>();
                        send_arg.put("type_", "custom");
                        send_arg.put("category", category);
                        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
                        Server_con serverCon = new Server_con(server_url, send_arg);
                        String result = serverCon.Receive_Server();
                        Log.d(TAG, "result = " + result);
                        if (result.equals("success")) {
                            Log.d(TAG, "Result -> success");
                            category = "";
                            SetInit();
                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        menu.findItem(R.id.month_view_btn).setVisible(false);
        menu.findItem(R.id.week_view_btn).setVisible(false);
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
//        int id = item.getItemId();
//        if (id == R.id.nav_information) {
//            // Handle the camera action
//        } else if (id == R.id.nav_notice) {
//
//        } else if (id == R.id.nav_usage) {
//
//        } else if (id == R.id.nav_score) {
//
//        } else if (id == R.id.nav_setup) {
//
//        } else if (id == R.id.nav_logout) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.pushOn:
                if (isChecked) {
                    Se_Application.Localdb.set_dataB("push", true);
                    pushOn.setTextColor(getResources().getColor(R.color.White));
                    pushOff.setTextColor(getResources().getColor(R.color.main_gray));
                    Log.d(TAG, "Push : " + Se_Application.Localdb.get_dataB("push"));
                } else {
                    Se_Application.Localdb.set_dataB("push", false);
                    pushOn.setTextColor(getResources().getColor(R.color.main_gray));
                    pushOff.setTextColor(getResources().getColor(R.color.White));
                    Log.d(TAG, "Push : " + Se_Application.Localdb.get_dataB("push"));
                }

                break;
            case R.id.pushOff:
                if (isChecked) {
                    Se_Application.Localdb.set_dataB("push", false);
                    pushOn.setTextColor(getResources().getColor(R.color.main_gray));
                    pushOff.setTextColor(getResources().getColor(R.color.White));
                    Log.d(TAG, "Push : " + Se_Application.Localdb.get_dataB("push"));
                } else {
                    Se_Application.Localdb.set_dataB("push", true);
                    pushOn.setTextColor(getResources().getColor(R.color.White));
                    pushOff.setTextColor(getResources().getColor(R.color.main_gray));
                    Log.d(TAG, "Push : " + Se_Application.Localdb.get_dataB("push"));
                }
                break;
        }
    }
}
