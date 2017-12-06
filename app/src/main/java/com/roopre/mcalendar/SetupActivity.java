package com.roopre.mcalendar;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private String TAG = "SetupActivity";
    Switch pushSwitch;
    RadioButton allRadio, customRadio;
    Menu menu;

    String category = "";
    GridView gridView;
    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> selectedList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        pushSwitch = (Switch) findViewById(R.id.push_switch);
        pushSwitch.setOnCheckedChangeListener(this);
        if (Se_Application.Localdb.get_dataB("push")) {
            pushSwitch.setChecked(true);
        } else {
            pushSwitch.setChecked(false);
        }
        allRadio = (RadioButton) findViewById(R.id.all_radiobtn);
        customRadio = (RadioButton) findViewById(R.id.custom_radiobtn);

        allRadio.setOnCheckedChangeListener(this);
        customRadio.setOnCheckedChangeListener(this);

        customRadio.setEnabled(false);

        gridView = (GridView) findViewById(R.id.category_gridview);

    }

    @Override
    public void onResume() {
        super.onResume();
        SetInit();
    }

    private void SetInit() {

        String server_url = "load_user_pick.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);
        if(Se_Application.strNotNull(result)){
            try{
                JSONArray jsonArray = new JSONArray(result);
                selectedList.clear();
                for(int i =0 ;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.getString("category").equals("all")){
                        allRadio.setChecked(true);
                        selectedList.add(jsonObject.getString("category"));
                    }
                    else
                    {
                        customRadio.setChecked(true);
                        selectedList.add(jsonObject.getString("category"));
                    }
                }
            } catch (Exception e){
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
                        customRadio.setChecked(true);
                        String server_url = "set_category.php";
                        HashMap<String, String> send_arg = new HashMap<String, String>();
                        send_arg.put("type_", "custom");
                        send_arg.put("category", category);
                        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
                        Server_con serverCon = new Server_con(server_url, send_arg);
                        String result = serverCon.Receive_Server();
                        Log.d(TAG, "result = "+result);
                        if(result.equals("success")){
                            Log.d(TAG, "Result -> success");
                            category = "";
                            customRadio.setChecked(true);
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
        int id = item.getItemId();
        if (id == R.id.nav_information) {
            // Handle the camera action
        } else if (id == R.id.nav_notice) {

        } else if (id == R.id.nav_usage) {

        } else if (id == R.id.nav_score) {

        } else if (id == R.id.nav_setup) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.push_switch:
                if (isChecked) {
                    Se_Application.Localdb.set_dataB("push", true);
                    Log.d(TAG, "Push : " + Se_Application.Localdb.get_dataB("push"));
                } else {
                    Se_Application.Localdb.set_dataB("push", false);
                    Log.d(TAG, "Push : " + Se_Application.Localdb.get_dataB("push"));
                }
                break;
            case R.id.all_radiobtn:
                if(isChecked){
                    Log.d(TAG, "all_radio_btn -> checked");
                    String server_url = "set_category.php";
                    HashMap<String, String> send_arg = new HashMap<String, String>();
                    send_arg.put("type_", "all");
                    send_arg.put("category", "");
                    send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
                    Server_con serverCon = new Server_con(server_url, send_arg);
                    String result = serverCon.Receive_Server();
                    Log.d(TAG, result);
                    SetInit();
                }
                else
                {
                    Log.d(TAG, "all_radio_btn -> unchecked");
                }
                break;
            case R.id.custom_radiobtn:
                if(isChecked){
                    Log.d(TAG, "custom_radio_btn -> checked");
                }
                else
                {
                    Log.d(TAG, "custom_radio_btn -> unchecked");
                }
                break;
        }
    }
}
