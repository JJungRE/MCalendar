package com.roopre.mcalendar;

import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jjungre on 2017. 12. 6..
 */

public class AllBenefitActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener {


    private String TAG = "AllBenefitActivity";
    Menu menu;

    String category = "";
    GridView skt_gridView;
    GridView kt_gridView;
    GridView lg_gridView;

    ArrayList<String> skt_categoryList = new ArrayList<String>();
    ArrayList<String> kt_categoryList = new ArrayList<String>();
    ArrayList<String> lg_categoryList = new ArrayList<String>();
    ArrayList<String> selectedList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Se_Application.preActivity.equals("Main")){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            Se_Application.preActivity = "";
        }
        else{
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            Se_Application.preActivity = "";
        }
        setContentView(R.layout.all_benefit);

        Log.d(TAG, "onCreate");
        final ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
        textviewTitle.setText("전체혜택 보기");
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.color.transparent);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_red)));

        skt_gridView = (GridView) findViewById(R.id.all_benefit_category_gv);
        kt_gridView = (GridView) findViewById(R.id.all_benefit_category_gv2);
        lg_gridView = (GridView) findViewById(R.id.all_benefit_category_gv3);

        SetInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");


    }
    private void SetInit() {
/*
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
//                for(int i =0 ;i<jsonArray.length();i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                    if(jsonObject.getString("category").equals("all")){
//                        allRadio.setChecked(true);
//                        selectedList.add(jsonObject.getString("category"));
//                    }
//                    else
//                    {
//                        customRadio.setChecked(true);
//                        selectedList.add(jsonObject.getString("category"));
//                    }
//                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
*/


        String server_url = "get_category.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
//        send_arg = new HashMap<String, String>();
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        //Log.d(TAG, result);

        if (Se_Application.strNotNull(result)) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                skt_categoryList.clear();
                kt_categoryList.clear();
                lg_categoryList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.getString("company").equals("skt")) {
                        skt_categoryList.add(jsonObject.getString("category"));

                    } else if (jsonObject.getString("company").equals("kt")) {
                        kt_categoryList.add(jsonObject.getString("category"));

                    } else if (jsonObject.getString("company").equals("lg")) {
                        lg_categoryList.add(jsonObject.getString("category"));

                    }

                }

                SetupCategoryAdapter setupCategoryAdapter = new SetupCategoryAdapter(this, skt_categoryList, selectedList);
                SetupCategoryAdapter setupCategoryAdapter2 = new SetupCategoryAdapter(this, kt_categoryList, selectedList);
                SetupCategoryAdapter setupCategoryAdapter3 = new SetupCategoryAdapter(this, lg_categoryList, selectedList);
                skt_gridView.setAdapter(setupCategoryAdapter);
                kt_gridView.setAdapter(setupCategoryAdapter2);
                lg_gridView.setAdapter(setupCategoryAdapter3);


                skt_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {
                        category = skt_categoryList.get(position);
                        Log.d(TAG, "Click => " + skt_categoryList.get(position));
//                        customRadio.setChecked(true);

                        Intent intent = new Intent(AllBenefitActivity.this, AllDetailActivity.class);
                        intent.putExtra("company", "SKT");
                        intent.putExtra("category", category);

                        startActivityForResult(intent,0);

                    }
                });
                kt_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {
                        category = kt_categoryList.get(position);
                        Log.d(TAG, "Click => " + kt_categoryList.get(position));
//                        customRadio.setChecked(true);

                        Intent intent = new Intent(AllBenefitActivity.this, AllDetailActivity.class);
                        intent.putExtra("company", "KT");
                        intent.putExtra("category", category);
                        startActivity(intent);

                    }
                });
                lg_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {
                        category = lg_categoryList.get(position);
                        Log.d(TAG, "Click => " + lg_categoryList.get(position));
//                        customRadio.setChecked(true);

                        Intent intent = new Intent(AllBenefitActivity.this, AllDetailActivity.class);
                        intent.putExtra("company", "LG");
                        intent.putExtra("category", category);
                        startActivity(intent);

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult");
        if (requestCode == 0) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        if (id == R.id.nav_information) {
//            // Handle the camera action
//        } else if (id == R.id.nav_notice) {
//
//        } else if (id == R.id.nav_usage) {
//
//        } else if (id == R.id.nav_score) {
//
//        } else if (id == R.id.nav_share) {
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
            /*
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
                if (isChecked) {
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
                */
        }
    }

}
