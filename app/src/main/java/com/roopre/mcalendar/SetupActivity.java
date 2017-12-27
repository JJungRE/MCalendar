package com.roopre.mcalendar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private String TAG = "SetupActivity";
    RadioButton pushOn, pushOff, allRadio, customRadio;
    Menu menu;

    String category = "";
    GridView gridView;
    Button kt_bt, skt_bt, lg_bt;
    FrameLayout category_fl;
    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> selectedList = new ArrayList<String>();

    TextView version_tv;

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

        version_tv = (TextView) findViewById(R.id.version_tv);
        PackageInfo info = null;
        String versionName = "";
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version_tv.setText(versionName);

        category_fl = (FrameLayout) findViewById(R.id.category_fl);
        gridView = (GridView) findViewById(R.id.category_gridview);
        allRadio = (RadioButton) findViewById(R.id.all_radiobtn);
        customRadio = (RadioButton) findViewById(R.id.custom_radiobtn);
        kt_bt = (Button) findViewById(R.id.kt_bt);
        skt_bt = (Button) findViewById(R.id.skt_bt);
        lg_bt = (Button) findViewById(R.id.lg_bt);

        kt_bt.setOnClickListener(this);
        skt_bt.setOnClickListener(this);
        lg_bt.setOnClickListener(this);

        allRadio.setOnCheckedChangeListener(this);
        customRadio.setOnCheckedChangeListener(this);


    }

    @Override
    public void onResume() {

        ButtonCheck();
        super.onResume();
    }

    private void ButtonCheck() {
        if (Se_Application.Localdb.get_dataS("company").equals("skt")) {
            skt_bt.setBackgroundResource(R.drawable.bg_category_red);

        } else if (Se_Application.Localdb.get_dataS("company").equals("kt")) {
            kt_bt.setBackgroundResource(R.drawable.bg_category_red);

        } else if (Se_Application.Localdb.get_dataS("company").equals("lg")) {
            lg_bt.setBackgroundResource(R.drawable.bg_category_red);
        } else {
            Log.d(TAG, "아직 선택된 통신사가 없음");
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.skt_bt:
                skt_bt.setBackgroundResource(R.drawable.bg_category_red);
                kt_bt.setBackgroundResource(R.drawable.border_button_grey);
                lg_bt.setBackgroundResource(R.drawable.border_button_grey);
                Se_Application.Localdb.set_dataS("company", "skt");
                CompanyProcess();
                SetInit();
                break;

            case R.id.kt_bt:
                skt_bt.setBackgroundResource(R.drawable.border_button_grey);
                kt_bt.setBackgroundResource(R.drawable.bg_category_red);
                lg_bt.setBackgroundResource(R.drawable.border_button_grey);
                Se_Application.Localdb.set_dataS("company", "kt");
                CompanyProcess();
                SetInit();
                break;

            case R.id.lg_bt:
                skt_bt.setBackgroundResource(R.drawable.border_button_grey);
                kt_bt.setBackgroundResource(R.drawable.border_button_grey);
                lg_bt.setBackgroundResource(R.drawable.bg_category_red);
                Se_Application.Localdb.set_dataS("company", "lg");
                CompanyProcess();
                SetInit();
                break;
        }

    }

    private void CompanyProcess() {
        String server_url = "set_company.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
        send_arg.put("company", Se_Application.Localdb.get_dataS("company"));
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);
        if (Se_Application.strNotNull(result)) {
            Log.d(TAG, "Company select complete!!");
        } else {
            Log.d(TAG, "Do not set");
        }
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

                    if (jsonArray.length()<2) {
                        allRadio.setChecked(true);
                        selectedList.add(jsonObject.getString("category"));
                    } else {
                        customRadio.setChecked(true);
                        selectedList.add(jsonObject.getString("category"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "PICK에 값이 없으면 안돼");
        }


        server_url = "get_select_category.php";
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
                        Log.d(TAG, "result = " + result);
                        if (result.equals("success")) {
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

        category_fl.setVisibility(View.VISIBLE);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

            case R.id.all_radiobtn:
                if (isChecked) {
                    Log.d(TAG, "all_radio_btn -> checked");
                    String server_url = "set_category.php";
                    HashMap<String, String> send_arg = new HashMap<String, String>();
                    send_arg.put("type_", "all");
                    send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
                    Server_con serverCon = new Server_con(server_url, send_arg);
                    String result = serverCon.Receive_Server();
                    Log.d(TAG + " > set_category", result);
                    SetInit();
                } else {
                    Log.d(TAG, "all_radio_btn -> unchecked");
                }
                break;

            case R.id.custom_radiobtn:
                if (isChecked) {
                    Log.d(TAG, "custom_radio_btn -> checked");
                } else {
                    Log.d(TAG, "custom_radio_btn -> unchecked");
                }
                break;
        }
    }

}
