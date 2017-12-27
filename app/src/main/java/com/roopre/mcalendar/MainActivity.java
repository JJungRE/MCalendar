package com.roopre.mcalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, View.OnTouchListener {
    SetMainScheduleAdapter mAdapter;
    private String TAG = "MainActivity";
    private ListView mListView;
    String point_ = "0", category, content, logo_img, memo, bt_confirm;
    TextView header_main_id, category_sd_tv, memo_sd_tv;
    ImageView img_sd_imav;
    LinearLayout header_linear;
    int count;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    ActionBar actionBar;
    TextView title_tv;
    Menu menu;
    private GestureDetectorCompat monthDetector;
    private GestureDetectorCompat weekDetector;
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

    int monthStartPosition = 0, monthEndPosition = 0;
    NavigationView main_drawer2;
    NavigationView main_drawer;

    Button viewBenefitBtn;
    private BackPressCloseHandler backPressCloseHandler;

    RotateLoading loading_bar;
    RelativeLayout loading_layout;
    //img_url Connecting..
    String subURL;
    private ProgressDialog progressDialog;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        loading_layout = (RelativeLayout) findViewById(R.id.loading_layout);

        mAdapter = new SetMainScheduleAdapter();
        cal = Calendar.getInstance();
        startCal = (Calendar) Se_Application.startCal.clone();
        endCal = (Calendar) Se_Application.endCal.clone();
        //2017-11-29 종민추가
        monthGridView = (GridView) findViewById(R.id.month_grid_view);
        weekGridView = (GridView) findViewById(R.id.week_grid_view);

        monthDetector = new GestureDetectorCompat(this, new MonthGestureListener());
        weekDetector = new GestureDetectorCompat(this, new WeekGestureListener());
        monthGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                monthDetector.onTouchEvent(event);
                return false;
            }
        });
        weekGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                weekDetector.onTouchEvent(event);
                return false;
            }
        });
        month_tv = (TextView) findViewById(R.id.month_tv);
        week_tv = (TextView) findViewById(R.id.week_tv);
        month_tv.setOnClickListener(this);
        week_tv.setOnClickListener(this);

        monthLeftBtn = (ImageButton) findViewById(R.id.month_left_btn);
        monthRightBtn = (ImageButton) findViewById(R.id.month_right_btn);
        weekLeftBtn = (ImageButton) findViewById(R.id.week_left_btn);
        weekRightBtn = (ImageButton) findViewById(R.id.week_right_btn);

        monthLeftBtn.setOnClickListener(this);
        monthRightBtn.setOnClickListener(this);
        weekLeftBtn.setOnClickListener(this);
        weekRightBtn.setOnClickListener(this);

        monthLeftBtn.setOnTouchListener(this);
        monthRightBtn.setOnTouchListener(this);
        weekLeftBtn.setOnTouchListener(this);
        weekRightBtn.setOnTouchListener(this);
        // 위젯과 멤버변수 참조 획득
        mListView = (ListView) findViewById(R.id.main_listview);

        viewBenefitBtn = (Button) findViewById(R.id.view_benefit_btn);
        viewBenefitBtn.setOnClickListener(this);

        // 아이템 추가 및 어댑터 등록
        backPressCloseHandler = new BackPressCloseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);

        actionBar = getSupportActionBar();
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.color.transparent);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_red)));

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

        //nav_header_main 부분 layout 가져오기
        View headerView = header_drawer.getHeaderView(0);
        header_linear = (LinearLayout) headerView.findViewById(R.id.header_linear);
        header_main_id = (TextView) headerView.findViewById(R.id.header_main_id);
        header_main_id.setText("ID : " + Se_Application.Localdb.get_dataS("userid"));
        header_linear.setOnClickListener(this);

        SetInit();
        setupWindowAnimations();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        monthDetector.onTouchEvent(event);
        weekDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void setupWindowAnimations() {
        Slide slide = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            slide = new Slide();
            slide.setDuration(1000);
            getWindow().setExitTransition(slide);

            getWindow().setReturnTransition(slide);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    private void SetInit() {
        monthStartPosition = 0;
        monthEndPosition = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            mAdapter.getmItems().clear();
        }
        dataSetting();
    }

    private void dataSetting() {

        final String server_url = "load_event.php";
        String urlSuffix = "";
        if (Se_Application.Localdb.get_dataS("month_week").equals("") || Se_Application.Localdb.get_dataS("month_week").equals("month")) {
            urlSuffix = urlSuffix + "?month_week=" + Se_Application.Localdb.get_dataS("month_week") + "&company=" + Se_Application.Localdb.get_dataS("company");
            String monthStartDay = String.format("%04d", cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.MONTH) + 1) + String.format("%02d", cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            String monthLastDay = String.format("%04d", cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.MONTH) + 1) + String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Log.d(TAG, "monthStartDay = " + monthStartDay);
            Log.d(TAG, "monthLastDay = " + monthLastDay);
            urlSuffix = urlSuffix + "&start=" + monthStartDay;
            urlSuffix = urlSuffix + "&last=" + monthLastDay;
        } else if (Se_Application.Localdb.get_dataS("month_week").equals("week")) {
            urlSuffix = urlSuffix + "?month_week=" + Se_Application.Localdb.get_dataS("month_week") + "&company=" + Se_Application.Localdb.get_dataS("company");
            String weekStartDay = String.format("%04d", startCal.get(Calendar.YEAR)) + String.format("%02d", startCal.get(Calendar.MONTH) + 1) + String.format("%02d", startCal.get(Calendar.DATE));
            String weekLastDay = String.format("%04d", endCal.get(Calendar.YEAR)) + String.format("%02d", endCal.get(Calendar.MONTH) + 1) + String.format("%02d", endCal.get(Calendar.DATE));
            Log.d(TAG, "weekStartDay = " + weekStartDay);
            Log.d(TAG, "weekLastDay = " + weekLastDay);
            urlSuffix = urlSuffix + "&start=" + weekStartDay;
            urlSuffix = urlSuffix + "&last=" + weekLastDay;
        }


        class Draw extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading_layout.setVisibility(View.VISIBLE);
            }


            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(Se_Application.Server_URL + server_url + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result = bufferedReader.readLine();
                    Log.d("result", result);
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
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

                    Se_Application.selectedYear = mYear;
                    Se_Application.selectedMonth = mMonth;

                    month_tv.setText(Integer.toString(mMonth) + "월");
                    Calendar mainCal = (Calendar) cal.clone();
                    mainCal.set(Calendar.DAY_OF_MONTH, 1);

                    int dayNum = mainCal.get(Calendar.DAY_OF_WEEK);

                    for (int i = 0; i < 7; i++) {
                        monthDayVO = new MonthDayVO();
                        monthDayVO.setTotal("0");
                        monthDayVO.setDay(Se_Application.daysEng[i]);
                        monthDays.add(monthDayVO);
                    }

                    for (int i = 0; i < dayNum - 1; i++) {
                        monthDayVO = new MonthDayVO();
                        monthDayVO.setDay("");
                        monthDayVO.setTotal("0");
                        monthDays.add(monthDayVO);
                    }
                    for (int i = 0; i < mainCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

                        if (monthStartPosition == 0) {
                            monthStartPosition = monthDays.size();
                        }
                        monthDayVO = new MonthDayVO();
                        monthDayVO.setDay(Integer.toString(i + 1));
                        monthDayVO.setTotal("0");
                        //Log.d(TAG, "monthDayVO day = " + monthDayVO.getDay());
                        monthDays.add(monthDayVO);
                    }
                    monthEndPosition = monthDays.size();

                    Log.d(TAG, "monthDays Size = " + monthDays.size());
                    if (monthDays.size() <= 25) {
                        while (monthDays.size() < 25) {
                            monthDayVO = new MonthDayVO();
                            monthDayVO.setDay("");
                            monthDayVO.setTotal("0");
                            monthDays.add(monthDayVO);
                        }
                    } else if (monthDays.size() > 25 && monthDays.size() <= 42) {
                        while (monthDays.size() < 42) {
                            monthDayVO = new MonthDayVO();
                            monthDayVO.setDay("");
                            monthDayVO.setTotal("0");
                            monthDays.add(monthDayVO);
                        }
                    } else if (monthDays.size() > 42 && monthDays.size() <= 49) {
                        while (monthDays.size() < 49) {
                            monthDayVO = new MonthDayVO();
                            monthDayVO.setDay("");
                            monthDayVO.setTotal("0");
                            monthDays.add(monthDayVO);
                        }
                    }
                    try {

                        JSONArray jarray = new JSONArray(s);
                        JSONObject jObject = null;

                        String tempCategory = "", tempTitle = "", tempTitle2 = "", tempLogoImg = "";
                        Bitmap tempBmp = null;
                        int count = 1;
                        String seq = "", category = "", title = "", startdate = "", enddate = "", logo_img = "";
                        String company = "";

                        for (int i = 0; i < jarray.length(); i++) {
                            jObject = jarray.getJSONObject(i);
                            seq = jObject.getString("seq");
                            title = jObject.getString("event_title");
                            startdate = jObject.getString("startdate");
                            enddate = jObject.getString("enddate");
                            category = jObject.getString("event_category");
                            logo_img = jObject.getString("logo_img");
                            company = jObject.getString("company");

                            int syear = Integer.parseInt(startdate.substring(0, 4));
                            int smonth = Integer.parseInt(startdate.substring(5, 7));
                            int sday = Integer.parseInt(startdate.substring(8, 10));
                            int eyear = Integer.parseInt(enddate.substring(0, 4));
                            int emonth = Integer.parseInt(enddate.substring(5, 7));
                            int eday = Integer.parseInt(enddate.substring(8, 10));

                            int resID = getResources().getIdentifier(logo_img, "drawable", "com.roopre.mcalendar");
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
                            Log.d(TAG, "syear = " + syear + ", smonth = " + smonth + " ,  sday = " + sday + ", eyear = " + eyear + " , emonth = " + emonth + " ,eday = " + eday);

                            for (int j = monthStartPosition; j < monthEndPosition; j++) {
                                if (cal.get(Calendar.YEAR) == syear) {
                                    if (cal.get(Calendar.MONTH) + 1 == smonth) {
                                        monthDayVO = monthDays.get(j);
                                        if (Integer.parseInt(monthDayVO.getDay()) >= sday) {
                                            if (cal.get(Calendar.YEAR) < eyear) {
                                                monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                                monthDayVO.getSeqList().add(seq);
                                                if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                    if (monthDayVO.getTitle1().equals("")) {
                                                        monthDayVO.setTitle1(title);
                                                    } else if (monthDayVO.getTitle2().equals("")) {
                                                        monthDayVO.setTitle2(title);
                                                    }
                                                }
                                                monthDays.set(j, monthDayVO);
                                            } else if (cal.get(Calendar.YEAR) == eyear) {
                                                if (cal.get(Calendar.MONTH) + 1 < emonth) {
                                                    monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                                    monthDayVO.getSeqList().add(seq);
                                                    if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                        if (monthDayVO.getTitle1().equals("")) {
                                                            monthDayVO.setTitle1(title);
                                                        } else if (monthDayVO.getTitle2().equals("")) {
                                                            monthDayVO.setTitle2(title);
                                                        }
                                                    }
                                                    monthDays.set(j, monthDayVO);
                                                } else if (cal.get(Calendar.MONTH) + 1 == emonth) {
                                                    if (Integer.parseInt(monthDayVO.getDay()) <= eday) {
                                                        monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                                        monthDayVO.getSeqList().add(seq);
                                                        if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                            if (monthDayVO.getTitle1().equals("")) {
                                                                monthDayVO.setTitle1(title);
                                                            } else if (monthDayVO.getTitle2().equals("")) {
                                                                monthDayVO.setTitle2(title);
                                                            }
                                                        }
                                                        monthDays.set(j, monthDayVO);
                                                    }
                                                }
                                            }
                                        }
                                    } else if (cal.get(Calendar.MONTH) + 1 > smonth) {
                                        monthDayVO = monthDays.get(j);
                                        if (cal.get(Calendar.YEAR) < eyear) {
                                            monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                            monthDayVO.getSeqList().add(seq);
                                            if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                if (monthDayVO.getTitle1().equals("")) {
                                                    monthDayVO.setTitle1(title);
                                                } else if (monthDayVO.getTitle2().equals("")) {
                                                    monthDayVO.setTitle2(title);
                                                }
                                            }
                                            monthDays.set(j, monthDayVO);
                                        } else if (cal.get(Calendar.YEAR) == eyear) {
                                            if (cal.get(Calendar.MONTH) + 1 < emonth) {
                                                monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                                monthDayVO.getSeqList().add(seq);
                                                if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                    if (monthDayVO.getTitle1().equals("")) {
                                                        monthDayVO.setTitle1(title);
                                                    } else if (monthDayVO.getTitle2().equals("")) {
                                                        monthDayVO.setTitle2(title);
                                                    }
                                                }
                                                monthDays.set(j, monthDayVO);
                                            } else if (cal.get(Calendar.MONTH) + 1 == emonth) {
                                                if (Integer.parseInt(monthDayVO.getDay()) <= eday) {
                                                    Log.d(TAG, "monthDayVO.getDay() = " + monthDayVO.getDay());
                                                    monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                                    monthDayVO.getSeqList().add(seq);
                                                    if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                        if (monthDayVO.getTitle1().equals("")) {
                                                            monthDayVO.setTitle1(title);
                                                        } else if (monthDayVO.getTitle2().equals("")) {
                                                            monthDayVO.setTitle2(title);
                                                        }
                                                    }
                                                    monthDays.set(j, monthDayVO);
                                                }
                                            }
                                        }
                                    }
                                } else if (cal.get(Calendar.YEAR) > syear) {
                                    monthDayVO = monthDays.get(j);
                                    if (cal.get(Calendar.YEAR) < eyear) {
                                        monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                        monthDayVO.getSeqList().add(seq);
                                        if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                            if (monthDayVO.getTitle1().equals("")) {
                                                monthDayVO.setTitle1(title);
                                            } else if (monthDayVO.getTitle2().equals("")) {
                                                monthDayVO.setTitle2(title);
                                            }
                                        }
                                        monthDays.set(j, monthDayVO);
                                    } else if (cal.get(Calendar.YEAR) == eyear) {
                                        if (cal.get(Calendar.MONTH) + 1 < emonth) {
                                            monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                            monthDayVO.getSeqList().add(seq);
                                            if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                if (monthDayVO.getTitle1().equals("")) {
                                                    monthDayVO.setTitle1(title);
                                                } else if (monthDayVO.getTitle2().equals("")) {
                                                    monthDayVO.setTitle2(title);
                                                }
                                            }
                                            monthDays.set(j, monthDayVO);
                                        } else if (cal.get(Calendar.MONTH) + 1 == emonth) {
                                            if (Integer.parseInt(monthDayVO.getDay()) <= eday) {
                                                monthDayVO.setTotal(Integer.toString(Integer.parseInt(monthDayVO.getTotal()) + 1));
                                                monthDayVO.getSeqList().add(seq);
                                                if (!(monthDayVO.getTitle1().equals(title) || monthDayVO.getTitle2().equals(title))) {
                                                    if (monthDayVO.getTitle1().equals("")) {
                                                        monthDayVO.setTitle1(title);
                                                    } else if (monthDayVO.getTitle2().equals("")) {
                                                        monthDayVO.setTitle2(title);
                                                    }
                                                }
                                                monthDays.set(j, monthDayVO);
                                            }
                                        }
                                    }
                                }
                            }

                    /*
                    Thread mThread = new Thread() {

                        @Override
                        public void run() {

                            try {

                                //  URL 주소를 이용해서 URL 객체 생성
                                URL url = new URL(logo_img);
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
                    mThread.join();*/

                            if (tempCategory.equals(category) && tempTitle.equals(title) && tempLogoImg.equals(logo_img)) {
                                Log.d(TAG, "&&& equals = " + category);
                            } else {
                                Log.d(TAG, "else = " + category);
                                mAdapter.addItem(Integer.toString(count), seq, bitmap, company, title);
                                count = count + 1;
                            }
                            tempBmp = bitmap;
                            tempCategory = category;
                            tempTitle = title;
                            tempLogoImg = logo_img;
                            Log.d(TAG, "category = " + category + ", title = " + title);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MonthCalendarAdapter monthCalendarAdapter = new MonthCalendarAdapter(getApplicationContext(), monthDays);
                    monthGridView.setAdapter(monthCalendarAdapter);
                    monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View view, int position, long id) {

                            if (monthDays.get(position).getSeqList().toString().length() > 2) {
                                Log.d(TAG, "Click Day => " + monthDays.get(position));
                                //Toast.makeText(MainActivity.this, monthDays.get(position).getSeqList().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ViewListActivity.class);
                                intent.putExtra("seq", monthDays.get(position).getSeqList().toString());
                                startActivity(intent);
                            }
                        }
                    });

                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View view, int position, long id) {
                            //Toast.makeText(MainActivity.this, mAdapter.getmItems().get(position).getSeq(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ViewDetailActivity.class);
                            intent.putExtra("seq", mAdapter.getmItems().get(position).getSeq().toString());
                            startActivity(intent);
                        }
                    });

                } else if (Se_Application.Localdb.get_dataS("month_week").equals("week")) {
                    CalendarView("week");
                    weekDays.clear();
                    int s_year = startCal.get(Calendar.YEAR);
                    int s_month = startCal.get(Calendar.MONTH) + 1;
                    int s_day = startCal.get(Calendar.DAY_OF_MONTH);

                    int e_year = endCal.get(Calendar.YEAR);
                    int e_month = endCal.get(Calendar.MONTH) + 1;
                    int e_day = endCal.get(Calendar.DAY_OF_MONTH);

                    startdt = String.format("%02d", s_month) + "/" + String.format("%02d", s_day);
                    enddt = String.format("%02d", e_month) + "/" + String.format("%02d", e_day);

                    week_tv.setText(startdt + "~" + enddt);

                    Calendar tempCal = (Calendar) startCal.clone();
                    for (int i = 0; i < 7; i++) {

                        weekDayVO = new WeekDayVO();
                        weekDayVO.setYear(String.valueOf(tempCal.get(Calendar.YEAR)));
                        weekDayVO.setMonth(String.valueOf(tempCal.get(Calendar.MONTH) + 1));
                        weekDayVO.setDay(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
                        weekDayVO.setTotal("0");
                        weekDays.add(weekDayVO);
                        tempCal.add(Calendar.DATE, 1);
                    }
                    try {
                        JSONArray jarray = new JSONArray(s);
                        JSONObject jObject = null;

                        String seq = "";
                        String category = "";
                        String title = "";
                        String startdate = "";
                        String enddate = "";
                        String logo_img = "";
                        String tempCategory = "", tempTitle = "", tempLogoImg = "";
                        Bitmap tempBmp = null;
                        String company = "";
                        int count = 1;
                        for (int i = 0; i < jarray.length(); i++) {

                            jObject = jarray.getJSONObject(i);
                            seq = jObject.getString("seq");
                            title = jObject.getString("event_title");
                            startdate = jObject.getString("startdate");
                            enddate = jObject.getString("enddate");
                            category = jObject.getString("event_category");
                            logo_img = jObject.getString("logo_img");
                            company = jObject.getString("company");

                            int syear = Integer.parseInt(startdate.substring(0, 4));
                            int smonth = Integer.parseInt(startdate.substring(5, 7));
                            int sday = Integer.parseInt(startdate.substring(8, 10));
                            int eyear = Integer.parseInt(enddate.substring(0, 4));
                            int emonth = Integer.parseInt(enddate.substring(5, 7));
                            int eday = Integer.parseInt(enddate.substring(8, 10));
                            int resID = getResources().getIdentifier(logo_img, "drawable", "com.roopre.mcalendar");
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);

                            for (int j = 0; j < 7; j++) {
                                if (Integer.parseInt(weekDays.get(j).getYear()) == syear) {
                                    if (Integer.parseInt(weekDays.get(j).getMonth()) == smonth) {
                                        if (Integer.parseInt(weekDays.get(j).getDay()) >= sday) {
                                            if (Integer.parseInt(weekDays.get(j).getYear()) < eyear) {
                                                weekDayVO = weekDays.get(j);
                                                weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                                weekDayVO.getSeqList().add(seq);
                                                if (weekDayVO.getImage1() == null) {
                                                    weekDayVO.setImage1(bitmap);
                                                } else {
                                                    if (weekDayVO.getImage1().equals(tempBmp)) {
                                                    } else {
                                                        if (weekDayVO.getImage2() == null) {
                                                            weekDayVO.setImage2(bitmap);
                                                        } else {
                                                            if (weekDayVO.getImage2().equals(tempBmp)) {
                                                            } else {
                                                                if (weekDayVO.getImage3() == null) {
                                                                    weekDayVO.setImage3(bitmap);
                                                                } else {
                                                                    if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                    } else {
                                                                        if (weekDayVO.getImage4() == null) {
                                                                            weekDayVO.setImage4(bitmap);
                                                                        } else {
                                                                            if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }


                                                    }
                                                }
                                                weekDays.set(j, weekDayVO);
                                            } else if (Integer.parseInt(weekDays.get(j).getYear()) == eyear) {
                                                if (Integer.parseInt(weekDays.get(j).getMonth()) < emonth) {
                                                    weekDayVO = weekDays.get(j);
                                                    weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                                    weekDayVO.getSeqList().add(seq);
                                                    if (weekDayVO.getImage1() == null) {
                                                        weekDayVO.setImage1(bitmap);
                                                    } else {
                                                        if (weekDayVO.getImage1().equals(tempBmp)) {
                                                        } else {
                                                            if (weekDayVO.getImage2() == null) {
                                                                weekDayVO.setImage2(bitmap);
                                                            } else {
                                                                if (weekDayVO.getImage2().equals(tempBmp)) {
                                                                } else {
                                                                    if (weekDayVO.getImage3() == null) {
                                                                        weekDayVO.setImage3(bitmap);
                                                                    } else {
                                                                        if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                        } else {
                                                                            if (weekDayVO.getImage4() == null) {
                                                                                weekDayVO.setImage4(bitmap);
                                                                            } else {
                                                                                if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                                }

                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }


                                                        }
                                                    }
                                                    weekDays.set(j, weekDayVO);
                                                } else if (Integer.parseInt(weekDays.get(j).getMonth()) == emonth) {
                                                    if (Integer.parseInt(weekDays.get(j).getDay()) <= eday) {
                                                        weekDayVO = weekDays.get(j);
                                                        weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                                        weekDayVO.getSeqList().add(seq);
                                                        if (weekDayVO.getImage1() == null) {
                                                            weekDayVO.setImage1(bitmap);
                                                        } else {
                                                            if (weekDayVO.getImage1().equals(tempBmp)) {
                                                            } else {
                                                                if (weekDayVO.getImage2() == null) {
                                                                    weekDayVO.setImage2(bitmap);
                                                                } else {
                                                                    if (weekDayVO.getImage2().equals(tempBmp)) {
                                                                    } else {
                                                                        if (weekDayVO.getImage3() == null) {
                                                                            weekDayVO.setImage3(bitmap);
                                                                        } else {
                                                                            if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                            } else {
                                                                                if (weekDayVO.getImage4() == null) {
                                                                                    weekDayVO.setImage4(bitmap);
                                                                                } else {
                                                                                    if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                                    }

                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }


                                                            }
                                                        }
                                                        weekDays.set(j, weekDayVO);
                                                    }
                                                }
                                            }

                                        }
                                    } else if (Integer.parseInt(weekDays.get(j).getMonth()) > smonth) {
                                        if (Integer.parseInt(weekDays.get(j).getYear()) < eyear) {
                                            weekDayVO = weekDays.get(j);
                                            weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                            weekDayVO.getSeqList().add(seq);
                                            if (weekDayVO.getImage1() == null) {
                                                weekDayVO.setImage1(bitmap);
                                            } else {
                                                if (weekDayVO.getImage1().equals(tempBmp)) {
                                                } else {
                                                    if (weekDayVO.getImage2() == null) {
                                                        weekDayVO.setImage2(bitmap);
                                                    } else {
                                                        if (weekDayVO.getImage2().equals(tempBmp)) {
                                                        } else {
                                                            if (weekDayVO.getImage3() == null) {
                                                                weekDayVO.setImage3(bitmap);
                                                            } else {
                                                                if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                } else {
                                                                    if (weekDayVO.getImage4() == null) {
                                                                        weekDayVO.setImage4(bitmap);
                                                                    } else {
                                                                        if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }


                                                }
                                            }
                                            weekDays.set(j, weekDayVO);
                                        } else if (Integer.parseInt(weekDays.get(j).getYear()) == eyear) {
                                            if (Integer.parseInt(weekDays.get(j).getMonth()) < emonth) {
                                                weekDayVO = weekDays.get(j);
                                                weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                                weekDayVO.getSeqList().add(seq);
                                                if (weekDayVO.getImage1() == null) {
                                                    weekDayVO.setImage1(bitmap);
                                                } else {
                                                    if (weekDayVO.getImage1().equals(tempBmp)) {
                                                    } else {
                                                        if (weekDayVO.getImage2() == null) {
                                                            weekDayVO.setImage2(bitmap);
                                                        } else {
                                                            if (weekDayVO.getImage2().equals(tempBmp)) {
                                                            } else {
                                                                if (weekDayVO.getImage3() == null) {
                                                                    weekDayVO.setImage3(bitmap);
                                                                } else {
                                                                    if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                    } else {
                                                                        if (weekDayVO.getImage4() == null) {
                                                                            weekDayVO.setImage4(bitmap);
                                                                        } else {
                                                                            if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }


                                                    }
                                                }
                                                weekDays.set(j, weekDayVO);
                                            } else if (Integer.parseInt(weekDays.get(j).getMonth()) == emonth) {
                                                if (Integer.parseInt(weekDays.get(j).getDay()) <= eday) {
                                                    weekDayVO = weekDays.get(j);
                                                    weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                                    weekDayVO.getSeqList().add(seq);
                                                    if (weekDayVO.getImage1() == null) {
                                                        weekDayVO.setImage1(bitmap);
                                                    } else {
                                                        if (weekDayVO.getImage1().equals(tempBmp)) {
                                                        } else {
                                                            if (weekDayVO.getImage2() == null) {
                                                                weekDayVO.setImage2(bitmap);
                                                            } else {
                                                                if (weekDayVO.getImage2().equals(tempBmp)) {
                                                                } else {
                                                                    if (weekDayVO.getImage3() == null) {
                                                                        weekDayVO.setImage3(bitmap);
                                                                    } else {
                                                                        if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                        } else {
                                                                            if (weekDayVO.getImage4() == null) {
                                                                                weekDayVO.setImage4(bitmap);
                                                                            } else {
                                                                                if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                                }

                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }


                                                        }
                                                    }
                                                    weekDays.set(j, weekDayVO);
                                                }
                                            }
                                        }
                                    }

                                } else if (Integer.parseInt(weekDays.get(j).getYear()) > syear) {
                                    if (Integer.parseInt(weekDays.get(j).getYear()) < eyear) {
                                        weekDayVO = weekDays.get(j);
                                        weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                        weekDayVO.getSeqList().add(seq);
                                        if (weekDayVO.getImage1() == null) {
                                            weekDayVO.setImage1(bitmap);
                                        } else {
                                            if (weekDayVO.getImage1().equals(tempBmp)) {
                                            } else {
                                                if (weekDayVO.getImage2() == null) {
                                                    weekDayVO.setImage2(bitmap);
                                                } else {
                                                    if (weekDayVO.getImage2().equals(tempBmp)) {
                                                    } else {
                                                        if (weekDayVO.getImage3() == null) {
                                                            weekDayVO.setImage3(bitmap);
                                                        } else {
                                                            if (weekDayVO.getImage3().equals(tempBmp)) {
                                                            } else {
                                                                if (weekDayVO.getImage4() == null) {
                                                                    weekDayVO.setImage4(bitmap);
                                                                } else {
                                                                    if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }


                                            }
                                        }
                                        weekDays.set(j, weekDayVO);
                                    } else if (Integer.parseInt(weekDays.get(j).getYear()) == eyear) {
                                        if (Integer.parseInt(weekDays.get(j).getMonth()) < emonth) {
                                            weekDayVO = weekDays.get(j);
                                            weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                            weekDayVO.getSeqList().add(seq);
                                            if (weekDayVO.getImage1() == null) {
                                                weekDayVO.setImage1(bitmap);
                                            } else {
                                                if (weekDayVO.getImage1().equals(tempBmp)) {
                                                } else {
                                                    if (weekDayVO.getImage2() == null) {
                                                        weekDayVO.setImage2(bitmap);
                                                    } else {
                                                        if (weekDayVO.getImage2().equals(tempBmp)) {
                                                        } else {
                                                            if (weekDayVO.getImage3() == null) {
                                                                weekDayVO.setImage3(bitmap);
                                                            } else {
                                                                if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                } else {
                                                                    if (weekDayVO.getImage4() == null) {
                                                                        weekDayVO.setImage4(bitmap);
                                                                    } else {
                                                                        if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }


                                                }
                                            }
                                            weekDays.set(j, weekDayVO);
                                        } else if (Integer.parseInt(weekDays.get(j).getMonth()) == emonth) {
                                            if (Integer.parseInt(weekDays.get(j).getDay()) <= eday) {
                                                weekDayVO = weekDays.get(j);
                                                weekDayVO.setTotal(Integer.toString(Integer.parseInt(weekDayVO.getTotal()) + 1));
                                                weekDayVO.getSeqList().add(seq);
                                                if (weekDayVO.getImage1() == null) {
                                                    weekDayVO.setImage1(bitmap);
                                                } else {
                                                    if (weekDayVO.getImage1().equals(tempBmp)) {
                                                    } else {
                                                        if (weekDayVO.getImage2() == null) {
                                                            weekDayVO.setImage2(bitmap);
                                                        } else {
                                                            if (weekDayVO.getImage2().equals(tempBmp)) {
                                                            } else {
                                                                if (weekDayVO.getImage3() == null) {
                                                                    weekDayVO.setImage3(bitmap);
                                                                } else {
                                                                    if (weekDayVO.getImage3().equals(tempBmp)) {
                                                                    } else {
                                                                        if (weekDayVO.getImage4() == null) {
                                                                            weekDayVO.setImage4(bitmap);
                                                                        } else {
                                                                            if (weekDayVO.getImage4().equals(tempBmp)) {
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                weekDays.set(j, weekDayVO);
                                            }
                                        }
                                    }
                                }
                            }

                            if (tempCategory.equals(category) && tempTitle.equals(title) && tempLogoImg.equals(logo_img)) {
                                //
                            } else {
                                mAdapter.addItem(Integer.toString(count), seq, bitmap, company, title);
                                count = count + 1;
                            }
                            tempBmp = bitmap;
                            tempCategory = category;
                            tempTitle = title;
                            tempLogoImg = logo_img;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    WeekCalendarAdapter weekCalendarAdapter = new WeekCalendarAdapter(getApplicationContext(), weekDays);
                    weekGridView.setAdapter(weekCalendarAdapter);
                    weekGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View view, int position, long id) {
                            if (weekDays.get(position).getSeqList().toString().length() > 2) {
                                //Toast.makeText(MainActivity.this, weekDays.get(position).getSeqList().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ViewListActivity.class);
                                intent.putExtra("seq", weekDays.get(position).getSeqList().toString());
                                startActivity(intent);
                            }
                        }
                    });
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View view, int position, long id) {

                            //Toast.makeText(MainActivity.this, mAdapter.getmItems().get(position).getSeq(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ViewDetailActivity.class);
                            intent.putExtra("seq", mAdapter.getmItems().get(position).getSeq().toString());
                            startActivity(intent);
                        }
                    });
                }

                if (getIntent().hasExtra("seq")) {
                    Log.d(TAG, "seq exist");
                    Intent intent = new Intent(MainActivity.this, ViewDetailActivity.class);
                    intent.putExtra("seq", getIntent().getStringExtra("seq"));
                    getIntent().removeExtra("seq");
                    startActivity(intent);
                }
                loading_layout.setVisibility(View.INVISIBLE);
            }

        }
        Draw draw = new Draw();
        draw.execute(urlSuffix);
    }

    private void CalendarView(String type_) {
        if (type_.equals("month")) {
            monthGridView.setVisibility(View.VISIBLE);
            monthLeftBtn.setVisibility(View.VISIBLE);
            month_tv.setVisibility(View.VISIBLE);
            monthRightBtn.setVisibility(View.VISIBLE);

            weekGridView.setVisibility(View.GONE);
            weekLeftBtn.setVisibility(View.GONE);
            week_tv.setVisibility(View.GONE);
            weekRightBtn.setVisibility(View.GONE);

        } else if (type_.equals("week")) {
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


    //phone 뒤로가기 버튼 코드
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > Se_Application.backKeyPressedTime + 2000) {
                Se_Application.backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= Se_Application.backKeyPressedTime + 2000) {
                finish();
            }
        }
    }

    //우측 상단 : 이런 모양 최초 선택시 호출
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        if (Se_Application.Localdb.get_dataS("month_week").equals("week")) {
            menu.findItem(R.id.month_view_btn).setVisible(true);
            menu.findItem(R.id.week_view_btn).setVisible(false);
        } else {
            menu.findItem(R.id.month_view_btn).setVisible(false);
            menu.findItem(R.id.week_view_btn).setVisible(true);
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        //this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

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
        switch (item.getItemId()) {
            case R.id.nav_notice:
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Toast.makeText(getApplicationContext(), "공지사항 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_score:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                //Toast.makeText(getApplicationContext(), "별점 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setup:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Toast.makeText(getApplicationContext(), "설정 선택", Toast.LENGTH_SHORT).show();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.month_tv:
                monthGridView.setVisibility(View.VISIBLE);
                break;
            case R.id.week_tv:
                weekGridView.setVisibility(View.VISIBLE);
                break;
            case R.id.month_left_btn:
                Log.d(TAG, "left");
                cal.add(Calendar.MONTH, -1);
                SetInit();
                //Toast.makeText(this, "Left Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.month_right_btn:
                Log.d(TAG, "right");
                cal.add(Calendar.MONTH, 1);
                SetInit();
                //Toast.makeText(this, "Right Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.week_left_btn:
                startCal.add(Calendar.DATE, -7);
                endCal.add(Calendar.DATE, -7);
                SetInit();
                //Toast.makeText(this, "Left Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.week_right_btn:
                startCal.add(Calendar.DATE, 7);
                endCal.add(Calendar.DATE, 7);
                SetInit();
                //Toast.makeText(this, "Right Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_benefit_btn:
                Se_Application.preActivity = "Main";
                startActivity(new Intent(MainActivity.this, AllBenefitActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                view.getBackground().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return false;
    }

    class MonthGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRightMonth();
                    } else {
                        onSwipeLeftMonth();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottomMonth();
                    } else {
                        onSwipeTopMonth();
                    }
                }
            }
            return true;
        }
    }

    private void onSwipeLeftMonth() {
        Log.d(TAG, "Month SwipeLeft()");
        cal.add(Calendar.MONTH, 1);
        SetInit();
    }

    private void onSwipeRightMonth() {
        Log.d(TAG, "Month SwipeRight()");
        cal.add(Calendar.MONTH, -1);
        SetInit();
    }

    private void onSwipeTopMonth() {
        Log.d(TAG, "Month SwipeTop()");
        monthGridView.setVisibility(View.GONE);
    }

    private void onSwipeBottomMonth() {
        Log.d(TAG, "Month SwipeBottom()");
        monthGridView.setVisibility(View.VISIBLE);
    }

    class WeekGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
            }
            return true;
        }
    }

    private void onSwipeLeft() {
        Log.d(TAG, "Week SwipeLeft()");
        startCal.add(Calendar.DATE, 7);
        endCal.add(Calendar.DATE, 7);
        SetInit();
    }

    private void onSwipeRight() {
        Log.d(TAG, "Week SwipeRight()");
        startCal.add(Calendar.DATE, -7);
        endCal.add(Calendar.DATE, -7);
        SetInit();
    }

    private void onSwipeTop() {
        Log.d(TAG, "Week SwipeTop()");
        weekGridView.setVisibility(View.GONE);
    }

    private void onSwipeBottom() {
        Log.d(TAG, "Week SwipeBottom()");
        weekGridView.setVisibility(View.VISIBLE);
    }
}
