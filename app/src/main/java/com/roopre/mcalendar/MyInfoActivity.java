package com.roopre.mcalendar;

import android.os.AsyncTask;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String TAG = "MyInfoActivity";
    LinearLayout class_linear, year_linear;
    String preClass = null, company, userid, group, point_, category, class_, type, pay, nickname;
    int year;
    TextView info_id_tv;
    EditText info_nick_et;
    private int selectedNum = -1;
    static final int[] Linears = {
            R.id.sk_ll,
            R.id.kt_ll,
            R.id.lg_ll
    };

    List<String> arrList = new ArrayList<String>();
    List<String> arrList2 = new ArrayList<String>();
    List<String> arrList3 = new ArrayList<String>();
    List<String> arrList4 = new ArrayList<String>();

    Spinner spinner, spinner2, spinner3, spinner4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);

        //actionBar 객체를 가져올 수 있다.
        ActionBar actionBar = getSupportActionBar();

        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        for (int linL : Linears) {
            LinearLayout lin = (LinearLayout) findViewById(linL);
            lin.setOnClickListener(this);
        }

        year_linear = (LinearLayout) findViewById(R.id.year_linear);

        spinner = (Spinner) findViewById(R.id.class_group_sp);
        spinner2 = (Spinner) findViewById(R.id.class_category_sp);
        spinner3 = (Spinner) findViewById(R.id.class_type_sp);
        spinner4 = (Spinner) findViewById(R.id.class_year_sp);

        class_linear = (LinearLayout) findViewById(R.id.class_linear);
        info_id_tv = (TextView) findViewById(R.id.info_id_tv);
        info_nick_et = (EditText) findViewById(R.id.info_nick_et);

        company = Se_Application.Localdb.get_dataS("company");
        userid = Se_Application.Localdb.get_dataS("userid");

        Log.d(TAG, userid);

        if (company.equals("skt")) {
            year_linear.setVisibility(View.VISIBLE);
        } else {
            year_linear.setVisibility(View.GONE);
        }

        String server_url = "class_form.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("company", company);
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);

        ClassProcess(result);

        Button modify = (Button) findViewById(R.id.info_mod_bt);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nickname.length() <= 2) {
                    Toast.makeText(MyInfoActivity.this, "닉네임은 2자 이상 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateClassProcess updateClassProcess = new UpdateClassProcess();
                    updateClassProcess.execute();
                }


            }
        });


    }

    //1. 요금제 종류 = group 부분 (LTE, 3G..)
    class SetClassProcess extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "[onPostExecute] " + result);

            InitData(result);

//            ClassFormProcess classFormProcess = new ClassFormProcess();
//            classFormProcess.execute();
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://52.79.209.10/spinner_group.php";
            String postParameters = "?company=" + company;

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("SPINNER_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result = bufferedReader.readLine();
                //출력
                Log.d("php result", "[doInBackgroud] " + result);

                return result;

            } catch (Exception e) {

                Log.d(TAG, "SetClassData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    //spinner부분 재설정
    private void InitData(String result) {

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;

            arrList.clear();
            arrList.add("요금제 종류");


            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                arrList.add(jsonObject.getString("group_"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //요금제 종류
        ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList);

        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(type_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (!(position == 0)) {

                    String str = (String) spinner.getSelectedItem();

                    Log.d("callingplan_sp1_select", parent.getItemAtPosition(position).toString());

                    group = parent.getItemAtPosition(position).toString();

//                    pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//                    // PREF_NAME, 즉 위에서 선언한 prevPage란 이름의 SharedPreferences를 사용하겠다.
//                    SharedPreferences.Editor editor = pref.edit();
//                    // SharedPreferences에 데이터를 넣기 위한 editor 선언
//                    editor.putString(SPREF_GROUP, group);
//                    // 에디터를 사용해서 PREF_CONFIRM, 즉 위에서 선언한 prev_confirm 키 에 None이라는 Value를 넣겠다.
//                    editor.commit();
//                    // 데이터 삽입

                    MyInfoActivity.SetClassProcess2 task2 = new MyInfoActivity.SetClassProcess2();
                    task2.execute(group);

                    arrList2.clear();
                    arrList2.add("요금제 분류");

                    arrList3.clear();
                    arrList3.add("요금제 세부항목");


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //요금제 분류
        arrList2.clear();
        arrList2.add("요금제 분류");

        ArrayAdapter<String> type_adapter2 = new ArrayAdapter<String>(MyInfoActivity.this,
                android.R.layout.simple_spinner_item, arrList2);

        type_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(type_adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //요금제 세부항목
        arrList3.clear();
        arrList3.add("요금제 세부항목");

        ArrayAdapter<String> type_adapter3 = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList3);

        type_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(type_adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //4. 요금제 기간 = year 부분
        ArrayAdapter<CharSequence> type_adapter4 = ArrayAdapter.createFromResource(MyInfoActivity.this,
                R.array.class_year, android.R.layout.simple_spinner_item);

        type_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(type_adapter4);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (!(position == 0)) {
                    if (position == 1) {
                        year = 0;
                    } else if (position == 2) {
                        year = 2;
                    } else if (position == 3) {
                        year = 5;
                    }

                    MyInfoActivity.LoadClassProcess loadClassProcess = new MyInfoActivity.LoadClassProcess();
                    loadClassProcess.execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }


    //2. 요금제 분류 = category 부분
    class SetClassProcess2 extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.i(TAG, "[onPostExecute2] " + result);

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    arrList2.add(jsonObject.getString("category"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> type_adapter2 = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList2);

            type_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(type_adapter2);

            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if (!(position == 0)) {

                        Log.d("callingplan_sp2_select", parent.getItemAtPosition(position).toString());

                        category = parent.getItemAtPosition(position).toString();

//                        pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//                        // PREF_NAME, 즉 위에서 선언한 prevPage란 이름의 SharedPreferences를 사용하겠다.
//                        SharedPreferences.Editor editor = pref.edit();
//                        // SharedPreferences에 데이터를 넣기 위한 editor 선언
//                        editor.putString(SPREF_CATEGORY, category);
//                        // 에디터를 사용해서 PREF_CONFIRM, 즉 위에서 선언한 prev_confirm 키 에 None이라는 Value를 넣겠다.
//                        editor.commit();
//                        // 데이터 삽입


                        MyInfoActivity.SetClassProcess3 task3 = new MyInfoActivity.SetClassProcess3();
                        task3.execute(category);

                        //요금제 세부항목
                        arrList3.clear();
                        arrList3.add("요금제 세부항목을 선택하세요");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://52.79.209.10/spinner_category.php";
            String postParameters = "?group=" + URLEncoder.encode(group) + "&company=" + company;

            BufferedReader bufferedReader2 = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("SPINNER_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader2 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result2 = bufferedReader2.readLine();
                //출력
                Log.d("php result", "[doInBackgroud2] " + result2);

                return result2;

            } catch (Exception e) {

                Log.d(TAG, "SetClassData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    //3. 요금제 세부항목 = type 부분
    class SetClassProcess3 extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.i(TAG, "[onPostExecute3] " + result);

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    arrList3.add(jsonObject.getString("type"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> type_adapter3 = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList3);

            type_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner3.setAdapter(type_adapter3);

            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if (!(position == 0)) {

                        String str = (String) spinner3.getSelectedItem();

                        Log.d("callingplan_sp3_select", parent.getItemAtPosition(position).toString());

                        type = parent.getItemAtPosition(position).toString();

//                        pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//                        // PREF_NAME, 즉 위에서 선언한 prevPage란 이름의 SharedPreferences를 사용하겠다.
//                        SharedPreferences.Editor editor = pref.edit();
//                        // SharedPreferences에 데이터를 넣기 위한 editor 선언
//                        editor.putString(SPREF_TYPE, type);
//                        // 에디터를 사용해서 PREF_CONFIRM, 즉 위에서 선언한 prev_confirm 키 에 None이라는 Value를 넣겠다.
//                        editor.commit();
//                        // 데이터 삽입

                        if (company.equals("lg") || company.equals("kt")) {
                            MyInfoActivity.LoadClassProcess loadClassProcess = new MyInfoActivity.LoadClassProcess();
                            loadClassProcess.execute(type);
                        }


                    }


                }


                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });


        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://52.79.209.10/spinner_type.php";
            String postParameters = "?category=" + URLEncoder.encode(category) + "&group=" + URLEncoder.encode(group) + "&company=" + company;

            BufferedReader bufferedReader3 = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("SPINNER_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader3 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result3 = bufferedReader3.readLine();
                //출력
                Log.d("php result", "[doInBackgroud3] " + result3);

                return result3;

            } catch (Exception e) {

                Log.d(TAG, "SetClassData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }


    //class_ 틀 불러오기
    private void ClassProcess(String result) {

        LinearLayout sk_ll = (LinearLayout) findViewById(R.id.sk_ll);
        LinearLayout kt_ll = (LinearLayout) findViewById(R.id.kt_ll);
        LinearLayout lg_ll = (LinearLayout) findViewById(R.id.lg_ll);

        switch (company) {
            case "skt":
                selectedNum = 1;
                sk_ll.setBackgroundResource(R.drawable.border_linear);
                break;
            case "kt":
                selectedNum = 2;
                kt_ll.setBackgroundResource(R.drawable.border_linear);
                break;
            case "lg":
                selectedNum = 3;
                lg_ll.setBackgroundResource(R.drawable.border_linear);
                break;
        }

        try {

            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;


            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                Button button = (Button) new Button(MyInfoActivity.this);
                LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                btnparams.weight = 1;
                button.setLayoutParams(btnparams);
                button.setText(jsonObject.getString("class_"));
                button.setTag(jsonObject.getString("class_"));
                if (Se_Application.Localdb.get_dataS("class").equals(""))
                    button.setBackgroundResource(R.drawable.border_button_white);
                button.setLines(1);
                button.setTextSize(getResources().getDimension(R.dimen.sp10));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (preClass != null) {
                            Button tempBtn = (Button) class_linear.findViewWithTag(preClass);
                            tempBtn.setBackgroundResource(R.drawable.border_button_white);
                        }
                        view.setBackgroundResource(R.drawable.border_button_orange);
//                        Toast.makeText(MyInfoActivity.this, (CharSequence) view.getTag(), Toast.LENGTH_SHORT).show();
                        preClass = (String) view.getTag();
                    }
                });

                //Log.d(TAG, "Button tag :"+button.getTag());

                class_linear.addView(button);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //초기값 Setting
        String server_url = "load_userinfo.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", userid);
        Server_con serverCon = new Server_con(server_url, send_arg);
        String infoResult = serverCon.Receive_Server();
        Log.d(TAG + " infoResult", infoResult);

        if (infoResult != null) {
            if (infoResult.equals("error data")) {
                Toast.makeText(getApplicationContext(), "회원 정보를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONArray jarray = new JSONArray(infoResult);
                    JSONObject jObject = null;
                    jObject = jarray.getJSONObject(0);

                    nickname = jObject.getString("nickname");

                    info_id_tv.setText(jObject.getString("userid"));
                    info_nick_et.setText(nickname);

                    preClass = jObject.getString("class_");
                    class_ = jObject.getString("class_");

                    arrList.add(jObject.getString("group_"));

                    arrList2.add(jObject.getString("category"));
                    category = jObject.getString("category");

                    arrList3.add(jObject.getString("type"));
                    type = jObject.getString("type");

                    //요금제 종류
                    ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList);
                    type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(type_adapter);

                    //요금제 분류
                    ArrayAdapter<String> type_adapter2 = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList2);
                    type_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner2.setAdapter(type_adapter2);

                    //요금제 세부항목
                    ArrayAdapter<String> type_adapter3 = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList3);
                    type_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner3.setAdapter(type_adapter3);

                    if (company.equals("skt")) {

                        String sp_year = jObject.getString("year");

                        switch (sp_year) {
                            case "0":
                                sp_year = "2년 미만";
                                break;
                            case "2":
                                sp_year = "2년 이상 ~ 5년 미만";
                                break;
                            case "5":
                                sp_year = "5년 이상";
                                break;

                        }

                        arrList4.add(sp_year);

                        //요금제 기간
                        ArrayAdapter<String> type_adapter4 = new ArrayAdapter<String>(MyInfoActivity.this, android.R.layout.simple_spinner_item, arrList4);
                        type_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner4.setAdapter(type_adapter4);
                    }

                    for (int i = 0; i < class_linear.getChildCount(); i++) {
                        Log.d(TAG, "child number : " + i);
                        Button tempBtn = (Button) class_linear.getChildAt(i);
                        Log.d(TAG + "infoResult:", tempBtn.getTag() + ", " + preClass);
                        if (preClass.equals(tempBtn.getTag() + "")) {
                            tempBtn.setBackgroundResource(R.drawable.border_button_orange);
                        }
                    }

                    Log.d("confirm", class_linear.getChildCount() + "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    //class_ 설정 해주기
    class LoadClassProcess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, result);

            if (company.equals("lg") || company.equals("kt")) {
                if (preClass != null) {
                    Button tempBtn = (Button) class_linear.findViewWithTag(preClass);
                    tempBtn.setBackgroundResource(R.drawable.border_button_grey);
                }
                Button mainBtn = (Button) class_linear.findViewWithTag(result);
                mainBtn.setBackgroundResource(R.drawable.border_button_orange);
                preClass = (String) result;
            } else if (company.equals("skt")) {
                if (preClass != null) {
                    Button tempBtn = (Button) class_linear.findViewWithTag(preClass);
                    tempBtn.setBackgroundResource(R.drawable.border_button_grey);
                }
                Button mainBtn = (Button) class_linear.findViewWithTag(result);
                mainBtn.setBackgroundResource(R.drawable.border_button_orange);
                preClass = (String) result;
            }

            class_ = result;


            Log.d("LoadClassProcess", class_);

            PayLoad payLoad = new PayLoad();
            payLoad.execute();

//            pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//            // PREF_NAME, 즉 위에서 선언한 prevPage란 이름의 SharedPreferences를 사용하겠다.
//            SharedPreferences.Editor editor = pref.edit();
//            // SharedPreferences에 데이터를 넣기 위한 editor 선언
//            editor.putString(SPREF_CLASS, class_);
//            // 에디터를 사용해서 PREF_CONFIRM, 즉 위에서 선언한 prev_confirm 키 에 None이라는 Value를 넣겠다.
//            editor.commit();
//            // 데이터 삽입


        }


        @Override
        protected String doInBackground(String... params) {

            String tempType = null;

            try {
                tempType = URLEncoder.encode(type, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String serverURL = "http://52.79.209.10/load_class.php";
            String postParameters = "?company=" + company + "&type=" + tempType + "&year=" + year;

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("LOAD_CLASS_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result = bufferedReader.readLine();
                //출력
                Log.d("php result", "[doInBackgroud] " + result);

                return result;

            } catch (Exception e) {
                Log.d(TAG, "SetClassData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }


    }

    //pay값 불러오기
    class PayLoad extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pay = result;

            Log.d("PayLoad", pay);

            MyInfoActivity.LoadPoint loadPoint = new MyInfoActivity.LoadPoint();
            loadPoint.execute();

//            pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//            // PREF_NAME, 즉 위에서 선언한 prevPage란 이름의 SharedPreferences를 사용하겠다.
//            SharedPreferences.Editor editor = pref.edit();
//            // SharedPreferences에 데이터를 넣기 위한 editor 선언
//            editor.putString(SPREF_PAY, pay);
//            // 에디터를 사용해서 PREF_CONFIRM, 즉 위에서 선언한 prev_confirm 키 에 None이라는 Value를 넣겠다.
//            editor.commit();
//            // 데이터 삽입

        }


        @Override
        protected String doInBackground(String... params) {


            String tempType = null;

            try {
                tempType = URLEncoder.encode(type, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String serverURL = "http://52.79.209.10/load_pay.php";
            String postParameters = "?company=" + company + "&type=" + tempType;

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("LOAD_CLASS_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result = bufferedReader.readLine();
                //출력
                Log.d("php result", "[doInBackgroud] " + result);

                return result;

            } catch (Exception e) {
                Log.d(TAG, "SetClassData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }


    }

    //고객 point값 불러오기
    class LoadPoint extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            point_ = result;


        }


        @Override
        protected String doInBackground(String... params) {
            String tempClass = null;


            try {
                tempClass = URLEncoder.encode(class_, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String serverURL = "http://52.79.209.10/load_point.php";
            String postParameters = "?class_=" + tempClass + "&company=" + company;

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("LOAD_POINT_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result = bufferedReader.readLine();
                //출력
                Log.d("php result", "[point] " + result);

                return result;

            } catch (Exception e) {

                Log.d(TAG, "SetClassData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    //준비된 자료 update 하기
    class UpdateClassProcess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("", result);

            if (result.equals("success")) {

                Se_Application.Localdb.set_dataS("userid", userid);
                Se_Application.Localdb.set_dataS("company", company);
                Se_Application.Localdb.set_dataS("class_", class_);
                Se_Application.Localdb.set_dataB("login", true);


                //Se_Application.Localdb.get_dataS("id");


//                pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString(SPREF_ID, userid);
//                editor.putString(SPREF_PW, passwd);
//                editor.putString(SPREF_COMPANY, company);
//                editor.putString(SPREF_LOGIN_TYPE, login);
//                editor.commit();

                MyInfoActivity.this.finish();

                Toast.makeText(getApplicationContext(), "수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

            } else if (result.equals("failed")) {
                Toast.makeText(getApplicationContext(), "서버오류", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "ID오류", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String M_nickname = info_nick_et.getText().toString();

/*
            if (!nickname.equals(M_nickname)) {

                String tempUserId = null;
                String tempNickName = null;

                try {
                    tempUserId = URLEncoder.encode(userid, "UTF-8");
                    tempNickName = URLEncoder.encode(M_nickname, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String serverURL = "http://52.79.209.10/update_nickname.php";
                String postParameters = "?userid=" + tempUserId + "&nickname=" + tempNickName;

                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(serverURL + postParameters);
                    Log.d("LOAD_CLASS_URL", url.toString());

                    //php url에 접속
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //그곳에 나오는 echo를 읽어
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    //그걸 읽어온 값
                    String result = bufferedReader.readLine();
                    //출력
                    Log.d("php result", "[doInBackgroud] " + result);

                    return result;

                } catch (Exception e) {
                    Log.d(TAG, "SetClassData: Error ", e);
                    return new String("Error: " + e.getMessage());
                }


            } else {
*/

            String tempUserId = null;
            String tempNickName = null;
            String tempCategory = null;
            String tempType = null;
            String tempClass = null;

            Log.d(TAG, "doInbackground -> try before");
            try {
                tempUserId = URLEncoder.encode(userid, "UTF-8");
                tempNickName = URLEncoder.encode(nickname, "UTF-8");
                tempCategory = URLEncoder.encode(category, "UTF-8");
                tempType = URLEncoder.encode(type, "UTF-8");
                tempClass = URLEncoder.encode(class_, "UTF-8");
                Log.d(TAG, "doInbackground -> try inner");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "doInbackground -> try before");

            String serverURL = "http://52.79.209.10/update_userinfo.php";
            String postParameters = "?company=" + company + "&group_=" + group + "&category=" + tempCategory + "&type=" + tempType + "&pay=" + pay + "&class_=" + tempClass
                    + "&point_=" + point_ + "&userid=" + tempUserId + "&nickname=" + tempNickName + "&year=" + year;

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("LOAD_CLASS_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result = bufferedReader.readLine();
                //출력
                Log.d("php result", "[doInBackgroud] " + result);

                return result;

            } catch (Exception e) {
                Log.d(TAG, "SetClassData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
            //  }

        }


    }

    //company Click Event.
    @Override
    public void onClick(View view) {
        LinearLayout sk_ll = (LinearLayout) findViewById(R.id.sk_ll);
        LinearLayout kt_ll = (LinearLayout) findViewById(R.id.kt_ll);
        LinearLayout lg_ll = (LinearLayout) findViewById(R.id.lg_ll);

        SetClassProcess tast = new SetClassProcess();
        tast.execute();

        //또 눌렸을때 값을 확인하고 Reset
        if (selectedNum == 1) {
            sk_ll.setBackgroundResource(R.drawable.border_black);
        } else if (selectedNum == 2) {
            kt_ll.setBackgroundResource(R.drawable.border_black);
        } else if (selectedNum == 3) {
            lg_ll.setBackgroundResource(R.drawable.border_black);
        }
        //눌린값을 변화시키고 Num값 부여
        switch (view.getId()) {
            case R.id.sk_ll:
                company = "skt";
                selectedNum = 1;
                sk_ll.setBackgroundResource(R.drawable.border_linear);
                year_linear.setVisibility(View.VISIBLE);
                break;
            case R.id.kt_ll:
                selectedNum = 2;
                company = "kt";
                kt_ll.setBackgroundResource(R.drawable.border_linear);
                year_linear.setVisibility(View.GONE);
                break;
            case R.id.lg_ll:
                selectedNum = 3;
                company = "lg";
                lg_ll.setBackgroundResource(R.drawable.border_linear);
                year_linear.setVisibility(View.GONE);
                break;
        }


        Log.d(TAG, company);

        return;


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
//        getMenuInflater().inflate(R.menu.main, menu);
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