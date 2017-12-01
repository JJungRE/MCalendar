package com.roopre.mcalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String TAG = "MainActivity";
    private ListView mListView;
    String point_ = "0", category, content, logo_img, memo, bt_confirm;
    TextView num_point_tv, header_main_id, category_sd_tv, memo_sd_tv;
    ImageView img_sd_imav;
    LinearLayout header_linear;
    int count;
    Button use_sd_bt, schedule_null;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    Menu menu;

    //2017-11-29 종민추가
    GridView monthGridView, weekGridView;
    String startdt, enddt;
    Calendar cal, startCal, endCal;
    TextView month_tv, week_tv;
    ImageButton monthLeftBtn, monthRightBtn, weekLeftBtn, weekRightBtn;
    ArrayList<MonthDayVO> monthDays = new ArrayList<MonthDayVO>();
    ArrayList<WeekDayVO> weekDays = new ArrayList<WeekDayVO>();
    MonthDayVO monthDayVO = new MonthDayVO();
    WeekDayVO weekDayVO = new WeekDayVO();
    int mYear, mMonth, mDay, mWeekDay, schedule_seq;
    LinearLayout monthLinear;

    NavigationView main_drawer2;
    NavigationView main_drawer;

    Button setScheduleBtn, viewBenefitBtn;
    private BackPressCloseHandler backPressCloseHandler;

    //img_url Connecting..
    public static final String baseCalendarURL = "https://s3.ap-northeast-2.amazonaws.com/mcalendar/";
    String subURL;
    private ProgressDialog progressDialog;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cal = Calendar.getInstance();

        startCal = (Calendar) Se_Application.startCal.clone();
        endCal = (Calendar) Se_Application.endCal.clone();
        //2017-11-29 종민추가
        monthGridView = (GridView) findViewById(R.id.month_grid_view);
        weekGridView = (GridView) findViewById(R.id.week_grid_view);

        monthLinear = (LinearLayout) findViewById(R.id.month_yoil_linear);

        month_tv = (TextView) findViewById(R.id.month_tv);
        week_tv = (TextView) findViewById(R.id.week_tv);

        monthLeftBtn = (ImageButton) findViewById(R.id.month_left_btn);
        monthRightBtn = (ImageButton) findViewById(R.id.month_right_btn);
        weekLeftBtn = (ImageButton) findViewById(R.id.week_left_btn);
        weekRightBtn = (ImageButton) findViewById(R.id.week_right_btn);

        monthLeftBtn.setOnClickListener(this);
        monthRightBtn.setOnClickListener(this);
        weekLeftBtn.setOnClickListener(this);
        weekRightBtn.setOnClickListener(this);
        // 위젯과 멤버변수 참조 획득
        mListView = (ListView) findViewById(R.id.set_main_schedule);


        setScheduleBtn = (Button) findViewById(R.id.set_schedule_btn);
        viewBenefitBtn = (Button) findViewById(R.id.view_benefit_btn);

        setScheduleBtn.setOnClickListener(this);
        viewBenefitBtn.setOnClickListener(this);

        // 아이템 추가 및 어댑터 등록
        dataSetting();
        backPressCloseHandler = new BackPressCloseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final NavigationView header_drawer = (NavigationView) findViewById(R.id.header_main);
        header_drawer.setNavigationItemSelectedListener(this);

        main_drawer = (NavigationView) findViewById(R.id.main_drawer1);
        main_drawer.setNavigationItemSelectedListener(this);

        main_drawer2 = (NavigationView) findViewById(R.id.main_drawer2);
        main_drawer2.setNavigationItemSelectedListener(this);


        num_point_tv = (TextView) findViewById(R.id.num_point_tv);

        //nav_header_main 부분 layout 가져오기
        View headerView = header_drawer.getHeaderView(0);
        header_linear = (LinearLayout) headerView.findViewById(R.id.header_linear);
        header_main_id = (TextView) headerView.findViewById(R.id.header_main_id);

        header_linear.setOnClickListener(this);


    }

    private void dataSetting() {
        final SetMainScheduleAdapter mMyAdapter = new SetMainScheduleAdapter();

        String server_url = "load_used_schedule.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
        send_arg.put("month", Integer.toString(Se_Application.mMonth));

        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();

        Log.d(TAG + "now", result+","+Se_Application.Localdb.get_dataS("userid"));

        if (result.equals("null") || result.equals("error data")) {
            Toast.makeText(MainActivity.this, "Schdule 내역이 없습니다.", Toast.LENGTH_SHORT).show();

            schedule_null = (Button) findViewById(R.id.schedule_null);
            schedule_null.setVisibility(View.VISIBLE);

            schedule_null.setText("등록된 일정이 없습니다.");

        } else {

            mListView.setVisibility(View.VISIBLE);
            try {

                JSONArray jarray = new JSONArray(result);
                JSONObject jObject = null;

                Log.d("now result: ", result);

                for (int i = 0; i < jarray.length(); i++) {

                    jObject = jarray.getJSONObject(i);

                    category = jObject.getString("category");
                    logo_img = jObject.getString("logo_img");
                    memo = jObject.getString("memo");
                    bt_confirm = jObject.getString("used");
                    schedule_seq = jObject.getInt("seq");

                    subURL = logo_img.substring(logo_img.indexOf("mcalendar/") + 10, logo_img.length());

                    send_arg.put("schedule_seq", Integer.toString(schedule_seq));

                    Log.d("now schedule_seq: ", send_arg.get("schedule_seq"));

                    //img_url Connecting..

                    // 안드로이드에서 네트워크 관련 작업을 할 때는
                    // 반드시 메인 스레드가 아닌 별도의 작업 스레드에서 작업해야 합니다.

                    Thread mThread = new Thread() {

                        @Override
                        public void run() {

                            try {

                                //  URL 주소를 이용해서 URL 객체 생성
                                URL url = new URL(baseCalendarURL + subURL);
                                Log.d("now url: ", String.valueOf(url));

                                // 아래 코드는 웹에서 이미지를 가져온 뒤
                                // 이미지 뷰에 지정할 Bitmap을 생성하는 과정

                                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();

                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);

                            } catch (IOException ex) {

                            }
                        }
                    };
                    // 웹에서 이미지를 가져오는 작업 스레드 실행.
                    mThread.start();

                    try {
                        // 메인 스레드는 작업 스레드가 이미지 작업을 가져올 때까지
                        // 대기해야 하므로 작업스레드의 join() 메소드를 호출해서
                        // 메인 스레드가 작업 스레드가 종료될 때까지 기다리도록 합니다.

                        mThread.join();
                        mMyAdapter.addItem(category, bitmap, memo, bt_confirm, Integer.toString(schedule_seq));

                        // 이제 작업 스레드에서 이미지를 불러오는 작업을 완료했기에
                        // UI 작업을 할 수 있는 메인스레드에서 이미지뷰에 이미지를 지정합니다.

                    } catch (InterruptedException e) {

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mListView.setAdapter(mMyAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        SetInit();
    }

    private void CalendarView(String type_) {
        if (type_.equals("month")) {
            monthLinear.setVisibility(View.VISIBLE);
            monthGridView.setVisibility(View.VISIBLE);
            monthLeftBtn.setVisibility(View.VISIBLE);
            month_tv.setVisibility(View.VISIBLE);
            monthRightBtn.setVisibility(View.VISIBLE);

            weekGridView.setVisibility(View.GONE);
            weekLeftBtn.setVisibility(View.GONE);
            week_tv.setVisibility(View.GONE);
            weekRightBtn.setVisibility(View.GONE);

        } else if (type_.equals("week")) {
            monthLinear.setVisibility(View.GONE);
            monthGridView.setVisibility(View.GONE);
            monthLeftBtn.setVisibility(View.GONE);
            month_tv.setVisibility(View.GONE);
            monthRightBtn.setVisibility(View.GONE);

            weekGridView.setVisibility(View.VISIBLE);
            weekLeftBtn.setVisibility(View.VISIBLE);
            week_tv.setVisibility(View.VISIBLE);
            weekRightBtn.setVisibility(View.VISIBLE);
        }
    }

    private void DrawCalendar() {

        Log.d(TAG, "month_week =" + Se_Application.Localdb.get_dataS("month_week"));
        if (Se_Application.Localdb.get_dataS("month_week").equals("") || Se_Application.Localdb.get_dataS("month_week").equals("month")) {
            CalendarView("month");
            monthDays.clear();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mMonth = mMonth + 1;
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            Log.d(TAG, "year = " + mYear + ", month = " + mMonth + ", day = " + mDay);

            month_tv.setText(Integer.toString(mMonth) + "월");
            Calendar mainCal = (Calendar) cal.clone();
            mainCal.set(Calendar.DAY_OF_MONTH, 1);

            int dayNum = mainCal.get(Calendar.DAY_OF_WEEK);

            for (int i = 0; i < dayNum - 1; i++) {
                monthDayVO = new MonthDayVO();
                monthDayVO.setDay("");
                monthDays.add(monthDayVO);
            }
            for (int i = 0; i < mainCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                monthDayVO = new MonthDayVO();
                monthDayVO.setDay(Integer.toString(i + 1));
                Log.d(TAG, "monthDayVO day = " + monthDayVO.getDay());
                monthDays.add(monthDayVO);
            }

            Log.d(TAG, "monthDays Size = " + monthDays.size());
            if (monthDays.size() <= 28) {
                while (monthDays.size() < 28) {
                    monthDayVO = new MonthDayVO();
                    monthDayVO.setDay("");
                    monthDays.add(monthDayVO);
                }
            } else if (monthDays.size() > 28 && monthDays.size() <= 35) {
                while (monthDays.size() < 35) {
                    monthDayVO = new MonthDayVO();
                    monthDayVO.setDay("");
                    monthDays.add(monthDayVO);
                }
            } else if (monthDays.size() > 35 && monthDays.size() <= 42) {
                while (monthDays.size() < 42) {
                    monthDayVO = new MonthDayVO();
                    monthDayVO.setDay("");
                    monthDays.add(monthDayVO);
                }
            }
            monthDayVO = new MonthDayVO();
            monthDayVO.setDay("25");
            monthDayVO.setTotal("2");
            monthDayVO.setImagePath1("11st");
            monthDayVO.setImagePath2("lotte");
            monthDays.set(27, monthDayVO);
            monthDayVO = new MonthDayVO();
            monthDayVO.setDay("27");
            monthDayVO.setTotal("3");
            monthDays.set(30, monthDayVO);

            Log.d(TAG, "monthDays Size = " + monthDays.size());

            for (int i = 0; i < monthDays.size(); i++) {
                Log.d(TAG, "monthdays get Day = " + monthDays.get(i).getDay());
            }

            MonthCalendarAdapter monthCalendarAdapter = new MonthCalendarAdapter(this, monthDays);
            monthGridView.setAdapter(monthCalendarAdapter);
            monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {

                    if (Se_Application.strNotNull(view.findViewById(R.id.day_linear).getTag().toString())) {
                        Log.d(TAG, "Click Day => " + monthDays.get(position));
                        Toast.makeText(MainActivity.this, view.findViewById(R.id.day_linear).getTag().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (Se_Application.Localdb.get_dataS("month_week").equals("week")) {
            CalendarView("week");
            weekDays.clear();

            int syear = startCal.get(Calendar.YEAR);
            int smonth = startCal.get(Calendar.MONTH) + 1;
            int sday = startCal.get(Calendar.DAY_OF_MONTH);

            int eyear = endCal.get(Calendar.YEAR);
            int emonth = endCal.get(Calendar.MONTH) + 1;
            int eday = endCal.get(Calendar.DAY_OF_MONTH);

            startdt = String.format("%02d", smonth) + "/" + String.format("%02d", sday);
            enddt = String.format("%02d", emonth) + "/" + String.format("%02d", eday);

            week_tv.setText(startdt + "~" + enddt);

            Calendar tempCal = (Calendar) startCal.clone();
            for (int i = 0; i < 7; i++) {

                weekDayVO = new WeekDayVO();
                weekDayVO.setYear(String.valueOf(tempCal.get(Calendar.YEAR)));
                weekDayVO.setMonth(String.valueOf(tempCal.get(Calendar.MONTH) + 1));
                weekDayVO.setDay(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
                weekDayVO.setTotal("2");
                weekDays.add(weekDayVO);
                tempCal.add(Calendar.DATE, 1);
            }
            weekDayVO = new WeekDayVO();
            weekDayVO.setYear("2017");
            weekDayVO.setMonth("11");
            weekDayVO.setDay("29");
            weekDayVO.setTotal("5");
            weekDays.set(3, weekDayVO);
            weekDayVO = new WeekDayVO();
            WeekCalendarAdapter weekCalendarAdapter = new WeekCalendarAdapter(this, weekDays);
            weekGridView.setAdapter(weekCalendarAdapter);
            weekGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {

                    if (Se_Application.strNotNull(view.findViewById(R.id.day_linear).getTag().toString())) {
                        Log.d(TAG, "Click Day => " + weekDays.get(position));
                        Toast.makeText(MainActivity.this, view.findViewById(R.id.day_linear).getTag().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    //로그인 유/무 처리하는 코드
    private void SetInit() {
//        spref= new SprefClass(this.getApplicationContext());
//        login = spref.getSprefBool("SPREF_LOGIN");
//
//        Log.d(TAG, login+"");

//        final NavigationView logout = (NavigationView) findViewById(R.id.nav_logout);

        DrawCalendar();

        if (Se_Application.Localdb.get_dataB("login")) {
            //로그인이 되어 있을 경우에는 현재 액티비티에서 다음 로직 수행
            if (main_drawer2.getMenu() != null) {
                Log.d(TAG, "login false -> nav_logout != null");
                main_drawer2.getMenu().findItem(R.id.nav_logout).setVisible(true);
            } else {
                Log.d(TAG, "login false -> nav_logout == null");
            }
            Log.d(TAG, "로그인 상태 유지");

            String server_url = "load_userinfo.php";
            HashMap<String, String> send_arg = new HashMap<String, String>();
            send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
            Server_con serverCon = new Server_con(server_url, send_arg);
            String result = serverCon.Receive_Server();
            Log.d(TAG, result);

            try {
                JSONArray jarray = new JSONArray(result);
                JSONObject jObject = null;
                jObject = jarray.getJSONObject(0);

                point_ = jObject.getString("point_");

                if (point_.equals("11")) {
                    num_point_tv.setText("무제한");
                } else {
                    num_point_tv.setText(point_);
                }
                header_main_id.setText(jObject.getString("userid"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            // 로그인이 안되어 있을 경우 로그인 화면 띄움
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
            if (main_drawer2.getMenu() != null) {
                Log.d(TAG, "login false -> nav_logout != null");
                main_drawer2.getMenu().findItem(R.id.nav_logout).setVisible(false);
            } else {
                Log.d(TAG, "login false -> nav_logout == null");
            }
        }
    }

    //phone 뒤로가기 버튼 코드
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    //우측 상단 : 이런 모양 최초 선택시 호출
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        if (Se_Application.Localdb.get_dataS("month_week").equals("month")) {
            menu.findItem(R.id.month_view_btn).setVisible(false);
            menu.findItem(R.id.week_view_btn).setVisible(true);
        } else {
            menu.findItem(R.id.month_view_btn).setVisible(true);
            menu.findItem(R.id.week_view_btn).setVisible(false);
        }

        return true;
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    //우측상단 : 이런 모양 메뉴 아이템 선택시 호출
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.week_view_btn:
                menu.findItem(R.id.week_view_btn).setVisible(false);
                menu.findItem(R.id.month_view_btn).setVisible(true);
                Se_Application.Localdb.set_dataS("month_week", "week");
                SetInit();
                break;
            case R.id.month_view_btn:
                menu.findItem(R.id.week_view_btn).setVisible(true);
                menu.findItem(R.id.month_view_btn).setVisible(false);
                Se_Application.Localdb.set_dataS("month_week", "month");
                SetInit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //왼쪽 네비게이션 메뉴 선택시 호출
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {

            case R.id.nav_information:
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                Toast.makeText(getApplicationContext(), "내정보 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_notice:
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
                Toast.makeText(getApplicationContext(), "공지사항 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_usage:
                startActivity(new Intent(MainActivity.this, ViewUsedActivity.class));
                Toast.makeText(getApplicationContext(), "사용내역 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_score:
                Toast.makeText(getApplicationContext(), "별점 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                Toast.makeText(getApplicationContext(), "설정 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                if (Se_Application.Localdb.get_dataB("login")) {
                    Se_Application.Localdb.set_dataB("login", false);

                    Log.d("onNavigationItemSelected: ", Se_Application.Localdb.get_dataB("login") + "");
                    Toast.makeText(MainActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    finish();

                    startActivity(new Intent(MainActivity.this, MainActivity.class));

//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(intent);
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_linear:
                drawer.closeDrawer(GravityCompat.START);
                if (!Se_Application.Localdb.get_dataB("login")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.month_left_btn:
                cal.add(Calendar.MONTH, -1);
                DrawCalendar();
                //Toast.makeText(this, "Left Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.month_right_btn:
                cal.add(Calendar.MONTH, 1);
                DrawCalendar();
                //Toast.makeText(this, "Right Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.week_left_btn:
                startCal.add(Calendar.DATE, -7);
                endCal.add(Calendar.DATE, -7);
                DrawCalendar();
                //Toast.makeText(this, "Left Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.week_right_btn:

                startCal.add(Calendar.DATE, 7);
                endCal.add(Calendar.DATE, 7);
                DrawCalendar();
                //Toast.makeText(this, "Right Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.set_schedule_btn:
                Intent intent = new Intent(this, RegScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.view_benefit_btn:
                break;
        }
    }
}
