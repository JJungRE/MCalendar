package com.roopre.mcalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegScheduleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private String TAG = "RegScheduleActivity";
    DatePickerDialog datePickerDialog;

    TextView date_tv, benefit_tv;
    EditText memo_et;
    Button cancelBtn, regBtn;
    Spinner categorySpinner, titleSpinner;
    String category = "", title = "";
    String logo_img = "";

    String year, month, day;


    List<String> categoryList = new ArrayList<String>();
    List<String> titleList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_schedule);
        Context context = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog);

        year = Integer.toString(Se_Application.mYear);
        month = Integer.toString(Se_Application.mMonth);
        day = Integer.toString(Se_Application.mDay);
        datePickerDialog = new DatePickerDialog(
                context, RegScheduleActivity.this, Se_Application.mYear, Se_Application.mMonth - 1, Se_Application.mDay);

        date_tv = (TextView) findViewById(R.id.date_tv);
        date_tv.setOnClickListener(this);

        date_tv.setText(Se_Application.FullDate(Se_Application.calendar));

        benefit_tv = (TextView) findViewById(R.id.benefit_tv);
        memo_et = (EditText) findViewById(R.id.memo_et);

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        regBtn = (Button) findViewById(R.id.reg_btn);
        cancelBtn.setOnClickListener(this);
        regBtn.setOnClickListener(this);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        titleSpinner = (Spinner) findViewById(R.id.title_spinner);

        InitCategory();
    }

    private void DataClear() {

        titleList.clear();
    }

    private void InitCategory() {

        String server_url = "get_category.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("company", Se_Application.Localdb.get_dataS("company"));
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);

        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                categoryList.add(jsonObject.getString("category"));

            }

            final ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, categoryList);

            category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(category_adapter);
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    category = (String) parent.getItemAtPosition(position);
                    Log.d(TAG, "category = " + category);
                    title = "";
                    DataClear();
                    InitTitle();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    category = "";
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void InitTitle() {

        String server_url = "get_title.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("company", Se_Application.Localdb.get_dataS("company"));
        send_arg.put("category", category);
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);

        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                titleList.add(jsonObject.getString("title"));

            }

            final ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, titleList);

            titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            titleSpinner.setAdapter(titleAdapter);
            titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    title = (String) parent.getItemAtPosition(position);
                    Log.d(TAG, "title = " + title);

                    InitBenefit();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    title = "";
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void InitBenefit() {

        String server_url = "get_benefit.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("company", Se_Application.Localdb.get_dataS("company"));
        send_arg.put("category", category);
        send_arg.put("title", title);
        send_arg.put("class_", Se_Application.Localdb.get_dataS("class_"));
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);

        if (result.length() >= 5) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    benefit_tv.setText(jsonObject.getString("benefit"));
                    logo_img = jsonObject.getString("logo_img");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            benefit_tv.setText("혜택이 없습니다");
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "year = " + year + ", month = " + (month - 1) + ", day = " + day);

        date_tv.setText(Se_Application.FullDate(year, month, day));
        this.year = Integer.toString(year);
        this.month = Integer.toString(month);
        this.day = Integer.toString(day);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_tv:
                datePickerDialog.show();
                break;
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.reg_btn:
                if(CheckValid()){
                    String server_url = "reg_schedule.php";
                    HashMap<String, String> send_arg = new HashMap<String, String>();
                    send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
                    send_arg.put("company", Se_Application.Localdb.get_dataS("company"));
                    send_arg.put("category", category);
                    send_arg.put("title", title);
                    send_arg.put("benefit", benefit_tv.getText().toString());
                    send_arg.put("logo_img", logo_img);
                    send_arg.put("year", year);
                    send_arg.put("month", month);
                    send_arg.put("day", day);
                    send_arg.put("memo", memo_et.getText().toString().length()<1?"":memo_et.getText().toString());


                    send_arg.put("memo", memo_et.getText().toString().length()<1?"":memo_et.getText().toString());
                    Log.d(TAG, Se_Application.Localdb.get_dataS("userid"));
                    Log.d(TAG, Se_Application.Localdb.get_dataS("company"));
                    Log.d(TAG, category);
                    Log.d(TAG, title);
                    Log.d(TAG, benefit_tv.getText().toString());
                    Log.d(TAG, logo_img);
                    Log.d(TAG, year);
                    Log.d(TAG, month);
                    Log.d(TAG, day);
                    Log.d(TAG, memo_et.getText().toString().length()<1?"":memo_et.getText().toString());
                    Server_con serverCon = new Server_con(server_url, send_arg);
                    String result = serverCon.Receive_Server();
                    Log.d(TAG, result);

                    if(result.equals("success")){
                        Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                    {
                        Toast.makeText(this, "실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private boolean CheckValid(){

        return true;
    }
}
