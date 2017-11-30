package com.roopre.mcalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SetClassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String TAG = "SetClassActivity";
    LinearLayout yearLinear, classLinear;
    String preclass = null;
    String userid, passwd, nickname;
    boolean login;
    Spinner spinner, spinner2, spinner3, spinner4;

    Button signupBtn, returnBtn;

    List<String> arrList = new ArrayList<String>();
    List<String> arrList2 = new ArrayList<String>();
    List<String> arrList3 = new ArrayList<String>();

    String company, group, category, type, class_, pay, point_;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_class);

        yearLinear = (LinearLayout) findViewById(R.id.year_linear);
        classLinear = (LinearLayout) findViewById(R.id.class_linear);

        spinner = (Spinner) findViewById(R.id.callingplan_sp1);
        spinner2 = (Spinner) findViewById(R.id.callingplan_sp2);
        spinner3 = (Spinner) findViewById(R.id.callingplan_sp3);
        spinner4 = (Spinner) findViewById(R.id.year_sp);

//        pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//        // PREF_NAME이라는 이름의 SharedPrefenreces를 불러온다.
//        String spref_com = pref.getString(SPREF_COMPANY, "");

        userid = Se_Application.userid;
        passwd = Se_Application.passwd;
        nickname = Se_Application.nickname;
        company = Se_Application.company;

        Log.d(TAG, "userid = "+userid +", passwd = "+passwd +", nickname = "+nickname +", company = "+company);


        if (company.equals("skt")) {
            yearLinear.setVisibility(View.VISIBLE);
        } else {
            yearLinear.setVisibility(View.GONE);
        }

        signupBtn = (Button) findViewById(R.id.class_next_bt);
        returnBtn = (Button) findViewById(R.id.class_back_bt);

        signupBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);

        SetClassProcess task = new SetClassProcess();
        task.execute(company);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.class_next_bt:
                InsertClassProcess insertClassProcess = new InsertClassProcess();
                insertClassProcess.execute();
                break;
            case R.id.class_back_bt:
                Intent intent = new Intent(SetClassActivity.this, SetCompanyActivity.class);
                SetClassActivity.this.startActivity(intent);
                SetClassActivity.this.finish();
                break;
        }
    }

    //1. 요금제 종류 = group 부분 (LTE, 3G..)
    class SetClassProcess extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "[onPostExecute] " + result);
            InitData(result);
        }
    }

    //spinner부분 재설정
    private void InitData(String result) {

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;


            arrList.add("요금제 종류를 선택하세요");

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                arrList.add(jsonObject.getString("group_"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //요금제 종류
        ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(SetClassActivity.this, R.layout.spinner_text, arrList);

        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(type_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (!(position == 0)) {

                    String str = (String) spinner.getSelectedItem();
                    Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString() + "을 선택하셨습니다", Toast.LENGTH_SHORT).show();

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

                    SetClassProcess2 task2 = new SetClassProcess2();
                    task2.execute(group);

                    arrList2.clear();
                    arrList2.add("요금제 분류를 선택하세요");

                    arrList3.clear();
                    arrList3.add("요금제 세부항목을 선택하세요");


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //요금제 분류
        arrList2.clear();
        arrList2.add("요금제 분류를 선택하세요");

        ArrayAdapter<String> type_adapter2 = new ArrayAdapter<String>(SetClassActivity.this,
                R.layout.spinner_text, arrList2);

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
        arrList3.add("요금제 세부항목을 선택하세요");

        ArrayAdapter<String> type_adapter3 = new ArrayAdapter<String>(SetClassActivity.this, R.layout.spinner_text, arrList3);

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
        ArrayAdapter<CharSequence> type_adapter4 = ArrayAdapter.createFromResource(SetClassActivity.this,
                R.array.class_year, R.layout.spinner_text);

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

                    LoadClassProcess loadClassProcess = new LoadClassProcess();
                    loadClassProcess.execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ClassFormProcess classFormProcess = new ClassFormProcess();
        classFormProcess.execute();
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
            ArrayAdapter<String> type_adapter2 = new ArrayAdapter<String>(SetClassActivity.this, R.layout.spinner_text, arrList2);

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


                        SetClassProcess3 task3 = new SetClassProcess3();
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
            ArrayAdapter<String> type_adapter3 = new ArrayAdapter<String>(SetClassActivity.this, R.layout.spinner_text, arrList3);

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
                            LoadClassProcess loadClassProcess = new LoadClassProcess();
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

    //5. 클래스 틀 불러오기
    class ClassFormProcess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }




        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://52.79.209.10/class_form.php";
            String postParameters = "?company=" + company;

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
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "LoadClassProcess, onPostExecute " + result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;


                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    Button button = (Button) new Button(SetClassActivity.this);
                    LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    btnparams.weight = 1;
                    button.setLayoutParams(btnparams);
                    button.setText(jsonObject.getString("class_"));
                    button.setTag(jsonObject.getString("class_"));
                    button.setBackgroundResource(R.drawable.border_button_grey);
                    button.setLines(1);
                    button.setTextSize(getResources().getDimension(R.dimen.sp10));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (preclass != null) {
                                Button tempBtn = (Button) classLinear.findViewWithTag(preclass);
                                tempBtn.setBackgroundResource(R.drawable.border_button_grey);
                            }
                            view.setBackgroundResource(R.drawable.border_button_orange);
                            preclass = (String) view.getTag();
                        }
                    });

                    classLinear.addView(button);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    //6. 클래스 설정 해주기
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
                if (preclass != null) {
                    Button tempBtn = (Button) classLinear.findViewWithTag(preclass);
                    tempBtn.setBackgroundResource(R.drawable.border_button_grey);
                }
                Button mainBtn = (Button) classLinear.findViewWithTag(result);
                mainBtn.setBackgroundResource(R.drawable.border_button_orange);
                preclass = (String) result;
            } else if (company.equals("skt")) {
                if (preclass != null) {
                    Button tempBtn = (Button) classLinear.findViewWithTag(preclass);
                    tempBtn.setBackgroundResource(R.drawable.border_button_grey);
                }
                Button mainBtn = (Button) classLinear.findViewWithTag(result);
                mainBtn.setBackgroundResource(R.drawable.border_button_orange);
                preclass = (String) result;
            }

            class_ = result;


            Log.d("LoadClassProcess", class_);

//            pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//            // PREF_NAME, 즉 위에서 선언한 prevPage란 이름의 SharedPreferences를 사용하겠다.
//            SharedPreferences.Editor editor = pref.edit();
//            // SharedPreferences에 데이터를 넣기 위한 editor 선언
//            editor.putString(SPREF_CLASS, class_);
//            // 에디터를 사용해서 PREF_CONFIRM, 즉 위에서 선언한 prev_confirm 키 에 None이라는 Value를 넣겠다.
//            editor.commit();
//            // 데이터 삽입


            PayLoad payLoad = new PayLoad();
            payLoad.execute();

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

    //7. pay값 불러오기
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

            LoadPoint loadPoint = new LoadPoint();
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

    //8.고객 point값 불러오기
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

    //9. 준비된 자료 저장해 주기
    class InsertClassProcess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }




        @Override
        protected String doInBackground(String... params) {

            String tempCategory = null;
            String tempType = null;
            String tempClass = null;
            String tempId = null;
            String tempNick = null;

            try {
                tempCategory = URLEncoder.encode(category, "UTF-8");
                tempType = URLEncoder.encode(type, "UTF-8");
                tempClass = URLEncoder.encode(class_, "UTF-8");
                tempId = URLEncoder.encode(userid, "UTF-8");
                tempNick = URLEncoder.encode(nickname, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String serverURL = "http://52.79.209.10/insert_class.php";
            String postParameters = "?company=" + company + "&group_=" + group + "&category=" + tempCategory + "&type=" + tempType + "&pay=" + pay + "&class_=" + tempClass
                    + "&userid=" + tempId + "&passwd=" + passwd + "&nickname=" + tempNick + "&point_=" + point_ + "&year=" + year;

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
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("", result);

            if (result.equals("success")) {

                Toast.makeText(SetClassActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                Se_Application.Localdb.set_dataS("userid", userid);
                Se_Application.Localdb.set_dataS("passwd", passwd);
                Se_Application.Localdb.set_dataS("nickname", nickname);
                Se_Application.Localdb.set_dataS("company", company);
                Se_Application.Localdb.set_dataS("class_", class_);
                Se_Application.Localdb.set_dataB("login", true);

                SetClassActivity.this.finish();

                //Se_Application.Localdb.get_dataS("id");


//                pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString(SPREF_ID, userid);
//                editor.putString(SPREF_PW, passwd);
//                editor.putString(SPREF_COMPANY, company);
//                editor.putString(SPREF_LOGIN_TYPE, login);
//                editor.commit();


            } else if (result.equals("failed")) {
                Toast.makeText(getApplicationContext(), "서버오류", Toast.LENGTH_SHORT).show();
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
