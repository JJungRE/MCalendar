package com.roopre.mcalendar;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewUsedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "ViewUsedActivity";


    TextView all_point;
    private LinearLayout main_cll;

    String point_;
    int used_years_point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_used);

        all_point = (TextView) findViewById(R.id.all_point);
        main_cll = (LinearLayout) findViewById(R.id.main_cll);

    }


    @Override
    public void onResume() {
        super.onResume();
        SetInit();
        SetUsed();
        //SetMonth();
    }

    private void SetInit() {

        String server_url = "load_userinfo.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", Se_Application.Localdb.get_dataS("userid"));
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();

        try {
            Log.d(TAG, result);
            JSONArray jarray = new JSONArray(result);
            JSONObject jObject = jarray.getJSONObject(0);
            point_ = jObject.getString("point_");
            all_point.setText(point_ + "P");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void SetUsed() {

        String server_url = "load_used_info.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("userid", Se_Application.Localdb.get_dataS("id"));
        send_arg.put("year", Integer.toString(Se_Application.mYear));

        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, "_result값: " + result);

        if (result.equals("null")) {
            Toast.makeText(this, "불러온 값이 없습니다.", Toast.LENGTH_SHORT).show();
        }

        try {

            LayoutInflater inflater = LayoutInflater.from(this);

            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;

            LinearLayout sub_cll, con_top_cll, con_cll;
            TextView month_tv = null, text_tv = null, month_point_tv = null, num_tv = null, date_tv = null, title_tv = null, point_tv = null, just_top_p = null, just_middle_p = null;

            LinearLayout sub_cll2, con_top_cll2, con_cll2;
            TextView month_tv2 = null, text_tv2 = null, month_point_tv2 = null, num_tv2 = null, date_tv2 = null, title_tv2 = null, point_tv2 = null, just_top_p2 = null, just_middle_p2 = null;

            String curmonth = "", premonth = "";
            int monthpoint = 0;

            //clone 할때 쓰이는 layout 부분
            sub_cll = (LinearLayout) inflater.inflate(R.layout.custom_used_view, null, false);

            //custom_used_view title 부분
            con_top_cll = (LinearLayout) sub_cll.findViewById(R.id.con_top_cll);

            //custom_used_view 아래 반복부분
            con_cll = (LinearLayout) sub_cll.findViewById(R.id.con_cll);

            //Layout 설정부분
            LinearLayout.LayoutParams subparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int sub_margin = getResources().getDimensionPixelSize(R.dimen.activity_half_horizontal_margin);
            subparams.setMargins(sub_margin, sub_margin, sub_margin, 0);

            sub_cll.setLayoutParams(subparams);

            sub_cll2 = new LinearLayout(this);
            con_top_cll2 = new LinearLayout(this);
            con_cll2 = new LinearLayout(this);

            //custom_used_view title 부분
            month_tv = (TextView) con_top_cll.findViewById(R.id.used_month_tv);
            text_tv = (TextView) con_top_cll.findViewById(R.id.used_text_tv);
            month_point_tv = (TextView) con_top_cll.findViewById(R.id.used_month_point_tv);
            just_top_p = (TextView) con_top_cll.findViewById(R.id.just_top_p);

            //custom_used_view 아래 반복부분
            num_tv = (TextView) con_cll.findViewById(R.id.used_num_tv);
            date_tv = (TextView) con_cll.findViewById(R.id.used_date_tv);
            title_tv = (TextView) con_cll.findViewById(R.id.used_title_tv);
            point_tv = (TextView) con_cll.findViewById(R.id.used_point_tv);
            just_middle_p = (TextView) con_cll.findViewById(R.id.just_middle_p);

            int textSize = getResources().getDimensionPixelSize(R.dimen.main_xsmall_text_size);
            main_cll.removeAllViews();
            int num = 0;
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                curmonth = jsonObject.getString("month");

                //마지막 일 때
                if (i == jsonArray.length() - 1) {
                    Log.d(TAG, "for -> 마지막 일 때");

                    //  같은 달일 때 (맨 마지막 달, 맨 마지막 줄)
                    if (curmonth.equals(premonth)) {
                        Log.d(TAG, "for -> 마지막 일 때 if문 -> curmonth = " + curmonth + " , premonth = " + premonth);

                        //처음 premonth=0이고 그 이후 부터는 마지막에 저장된 month값
                        premonth = jsonObject.getString("month");
                        Log.d(TAG, "for -> 마지막 일 때 ->  같은 달일 때");

                        con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_middle));
                        sub_cll2.addView(con_cll2);
                        con_cll2 = new LinearLayout(this);
                        con_cll2.setLayoutParams(con_cll.getLayoutParams());
                        con_cll2.setBackground(con_cll.getBackground());
                        con_cll2.setOrientation(con_cll.getOrientation());

                        num_tv2 = new TextView(this);
                        date_tv2 = new TextView(this);
                        title_tv2 = new TextView(this);
                        point_tv2 = new TextView(this);
                        just_middle_p2 = new TextView(this);


                        num_tv2 = this.CloneTV(num_tv, num_tv2, textSize);
                        date_tv2 = this.CloneTV(date_tv, date_tv2, textSize);
                        title_tv2 = this.CloneTV(title_tv, title_tv2, textSize);
                        point_tv2 = this.CloneTV(point_tv, point_tv2, textSize);
                        just_middle_p2 = this.CloneTV(just_middle_p, just_middle_p2, textSize);

                        num = num + 1;
                        num_tv2.setText(Integer.toString(num));
                        date_tv2.setText(jsonObject.getString("mkdate"));
                        title_tv2.setText(jsonObject.getString("title"));
                        point_tv2.setText(jsonObject.getString("used_point"));
                        just_middle_p2.setText("P");

                        //마지막줄 까지 최종 계산하고 총합을 used_month_point_tv에 입력.
                        monthpoint = monthpoint + jsonObject.getInt("used_point");
                        used_years_point = used_years_point + jsonObject.getInt("used_point");
                        month_point_tv2.setText(Integer.toString(monthpoint));


                        con_cll2.addView(num_tv2);
                        con_cll2.addView(date_tv2);
                        con_cll2.addView(title_tv2);
                        con_cll2.addView(point_tv2);
                        con_cll2.addView(just_middle_p2);

                        con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_bottom));
                        sub_cll2.addView(con_cll2);

                        main_cll.addView(sub_cll2);
                    }

                    // 다른 달일 때
                    else {
//                        num = 0;
//                        Log.d(TAG, "curmonth = " + curmonth + " , premonth = " + premonth);
//
//                        con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_bottom));
//                        sub_cll2.addView(con_cll2);
//                        main_cll.addView(sub_cll2);
//                        monthpoint = 0;
//                        premonth = jsonObject.getString("month");
//                        Log.d(TAG, "for -> 마지막 일 때 ->  다른 달일 때");
//
//                        sub_cll2 = new LinearLayout(this);
//                        sub_cll2.setLayoutParams(sub_cll.getLayoutParams());
//                        sub_cll2.setBackground(sub_cll.getBackground());
//                        sub_cll2.setOrientation(sub_cll.getOrientation());
//
//                        con_top_cll2 = new LinearLayout(this);
//                        con_top_cll2.setLayoutParams(con_top_cll.getLayoutParams());
//                        con_top_cll2.setBackground(con_top_cll.getBackground());
//                        con_top_cll2.setOrientation(con_top_cll.getOrientation());
//
//                        con_cll2 = new LinearLayout(this);
//                        con_cll2.setLayoutParams(con_cll.getLayoutParams());
//                        con_cll2.setBackground(con_cll.getBackground());
//                        con_cll2.setOrientation(con_cll.getOrientation());
//
//                        month_tv2 = new TextView(this);
//                        text_tv2 = new TextView(this);
//                        month_point_tv2 = new TextView(this);
//
//                        num_tv2 = new TextView(this);
//                        date_tv2 = new TextView(this);
//                        title_tv2 = new TextView(this);
//                        point_tv2 = new TextView(this);
//                        just_middle_p2 = new TextView(this);
//
//
//                        month_tv2 = this.CloneTV(month_tv, month_tv2, textSize);
//                        text_tv2 = this.CloneTV(text_tv, text_tv2, textSize);
//                        month_point_tv2 = this.CloneTV(month_point_tv, month_point_tv2, textSize);
//
//                        num_tv2 = this.CloneTV(num_tv, num_tv2, textSize);
//                        date_tv2 = this.CloneTV(date_tv, date_tv2, textSize);
//                        title_tv2 = this.CloneTV(title_tv, title_tv2, textSize);
//                        point_tv2 = this.CloneTV(point_tv, point_tv2, textSize);
//                        just_middle_p2 = this.CloneTV(just_middle_p, just_middle_p2, textSize);
//
//                        monthpoint = monthpoint + jsonObject.getInt("used_point");
//                        month_point_tv2.setText(Integer.toString(monthpoint));
//                        month_tv2.setText(jsonObject.getString("month"));
//                        text_tv2.setText("이번 달 태운 포인트");
//
//                        num = num + 1;
//                        num_tv2.setText(Integer.toString(num));
//                        date_tv2.setText(jsonObject.getString("mkdate"));
//                        title_tv2.setText(jsonObject.getString("title"));
//                        point_tv2.setText(jsonObject.getString("used_point"));
//                        just_middle_p2.setText("P");
//
//                        con_top_cll2.addView(month_tv2);
//                        con_top_cll2.addView(text_tv2);
//                        con_top_cll2.addView(month_point_tv2);
//
//                        con_cll2.addView(num_tv2);
//                        con_cll2.addView(date_tv2);
//                        con_cll2.addView(title_tv2);
//                        con_cll2.addView(point_tv2);
//                        con_cll2.addView(just_middle_p2);
//
//                        con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_bottom));
//                        sub_cll2.addView(con_top_cll2);
//                        sub_cll2.addView(con_cll2);
//
//                        main_cll.addView(sub_cll2);


                    }

                }
                // 마지막이 아닐 때
                else {
                    Log.d(TAG, "for -> 마지막이 아닐 때");

                    //  전체 mold 에서 List 중간 부분.
                    if (curmonth.equals(premonth)) {
                        Log.d(TAG, "curmonth = " + curmonth + " , premonth = " + premonth);
                        if (sub_cll2.getChildCount() == 0) {
                            con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_middle));
                            sub_cll2.addView(con_top_cll2);
                        }
                        sub_cll2.addView(con_cll2);
                        //sub_cll2.setBackgroundColor(Color.BLACK);
                        premonth = jsonObject.getString("month");
                        Log.d(TAG, "for -> 마지막이 아닐 때 -> 같은 달일 때");

                        con_cll2 = new LinearLayout(this);
                        con_cll2.setLayoutParams(con_cll.getLayoutParams());
                        con_cll2.setBackground(con_cll.getBackground());
                        con_cll2.setOrientation(con_cll.getOrientation());

                        num_tv2 = new TextView(this);
                        date_tv2 = new TextView(this);
                        title_tv2 = new TextView(this);
                        point_tv2 = new TextView(this);
                        just_middle_p2 = new TextView(this);

                        num_tv2 = this.CloneTV(num_tv, num_tv2, textSize);
                        date_tv2 = this.CloneTV(date_tv, date_tv2, textSize);
                        title_tv2 = this.CloneTV(title_tv, title_tv2, textSize);
                        point_tv2 = this.CloneTV(point_tv, point_tv2, textSize);
                        just_middle_p2 = this.CloneTV(just_middle_p, just_middle_p2, textSize);

                        num = num + 1;
                        num_tv2.setText(Integer.toString(num));
                        date_tv2.setText(jsonObject.getString("mkdate"));
                        title_tv2.setText(jsonObject.getString("title"));
                        point_tv2.setText(jsonObject.getString("used_point"));
                        just_middle_p2.setText("P");

                        //point 첫줄, 마지막줄 빼고 계산부분
                        monthpoint = monthpoint + jsonObject.getInt("used_point");
                        month_point_tv2.setText(Integer.toString(monthpoint));

                        used_years_point = used_years_point + jsonObject.getInt("used_point");


                        con_cll2.addView(num_tv2);
                        con_cll2.addView(date_tv2);
                        con_cll2.addView(title_tv2);
                        con_cll2.addView(point_tv2);
                        con_cll2.addView(just_middle_p2);

                        con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_middle));
                        Log.d(TAG, "sub_cll2.getChildCount = " + sub_cll2.getChildCount());

                    }
                    // 모든 mold 첫번째 시작, cll_top_cll 부분 전부와 List 첫번째 줄 구현
                    else {
                        num = 0;
                        monthpoint = 0;
                        if (i != 0) {
                            Log.d(TAG, "마지막이 아니고 다른 달 일 때 i !=0");
                            con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_bottom));
                            sub_cll2.addView(con_cll2);
                            main_cll.addView(sub_cll2);

                        }

                        premonth = jsonObject.getString("month");
                        Log.d(TAG, "curmonth = " + curmonth + " , premonth = " + premonth);
                        Log.d(TAG, "for -> 마지막 이 아닐 때 ->  다른 달일 때");

                        sub_cll2 = new LinearLayout(this);
                        sub_cll2.setLayoutParams(sub_cll.getLayoutParams());
                        sub_cll2.setBackground(sub_cll.getBackground());
                        sub_cll2.setOrientation(sub_cll.getOrientation());

                        con_top_cll2 = new LinearLayout(this);
                        con_top_cll2.setLayoutParams(con_top_cll.getLayoutParams());
                        con_top_cll2.setBackground(con_top_cll.getBackground());
                        con_top_cll2.setOrientation(con_top_cll.getOrientation());

                        con_cll2 = new LinearLayout(this);
                        con_cll2.setLayoutParams(con_cll.getLayoutParams());
                        con_cll2.setBackground(con_cll.getBackground());
                        con_cll2.setOrientation(con_cll.getOrientation());


                        month_tv2 = new TextView(this);
                        text_tv2 = new TextView(this);
                        month_point_tv2 = new TextView(this);
                        just_top_p2 = new TextView(this);

                        num_tv2 = new TextView(this);
                        date_tv2 = new TextView(this);
                        title_tv2 = new TextView(this);
                        point_tv2 = new TextView(this);
                        just_middle_p2 = new TextView(this);


                        month_tv2 = this.CloneTV(month_tv, month_tv2, textSize);
                        text_tv2 = this.CloneTV(text_tv, text_tv2, textSize);
                        month_point_tv2 = this.CloneTV(month_point_tv, month_point_tv2, textSize);
                        just_top_p2 = this.CloneTV(just_top_p, just_top_p2, textSize);

                        num_tv2 = this.CloneTV(num_tv, num_tv2, textSize);
                        date_tv2 = this.CloneTV(date_tv, date_tv2, textSize);
                        title_tv2 = this.CloneTV(title_tv, title_tv2, textSize);
                        point_tv2 = this.CloneTV(point_tv, point_tv2, textSize);
                        just_middle_p2 = this.CloneTV(just_middle_p, just_middle_p2, textSize);

                        month_tv2.setText(jsonObject.getString("month"));
                        text_tv2.setText("이번 달 태운 포인트");
                        just_top_p2.setText("P");

                        num = num + 1;
                        num_tv2.setText(Integer.toString(num));
                        date_tv2.setText(jsonObject.getString("mkdate"));
                        title_tv2.setText(jsonObject.getString("title"));
                        point_tv2.setText(jsonObject.getString("used_point"));
                        just_middle_p2.setText("P");

                        //point 계산부분
                        monthpoint = monthpoint + Integer.parseInt(jsonObject.getString("used_point"));
                        month_point_tv2.setText(Integer.toString(monthpoint));

                        used_years_point = used_years_point + jsonObject.getInt("used_point");


                        con_top_cll2.addView(month_tv2);
                        con_top_cll2.addView(text_tv2);
                        con_top_cll2.addView(month_point_tv2);
                        con_top_cll2.addView(just_top_p2);

                        con_cll2.addView(num_tv2);
                        con_cll2.addView(date_tv2);
                        con_cll2.addView(title_tv2);
                        con_cll2.addView(point_tv2);
                        con_cll2.addView(just_middle_p2);

                        con_cll2.setBackground(getResources().getDrawable(R.drawable.border_view_used_middle));
                        sub_cll2.addView(con_top_cll2);

                        Log.d(TAG, "for -> 마지막 이 아닐 때 ->  다른 달일 때");
                    }

                }

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }


        TextView years_point = (TextView) findViewById(R.id.used_years_point_tv);
        years_point.setText(Integer.toString(used_years_point) + "P");

        int remain_point = 0;

        remain_point = Integer.parseInt(point_) - used_years_point;

        TextView remain_point_tv = (TextView) findViewById(R.id.remain_point_tv);
        remain_point_tv.setText(Integer.toString(remain_point) + "P");

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private TextView CloneTV(TextView originalTV, TextView cloneTV, int textSize) {
        cloneTV = new TextView(this);
        cloneTV.setLayoutParams(originalTV.getLayoutParams());
        cloneTV.setBackground(originalTV.getBackground());
        cloneTV.setTextColor(originalTV.getCurrentTextColor());
        cloneTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        cloneTV.setTypeface(originalTV.getTypeface());
        cloneTV.setGravity(originalTV.getGravity());
        cloneTV.setPadding(originalTV.getPaddingLeft(), originalTV.getPaddingTop(), originalTV.getPaddingRight(), originalTV.getPaddingBottom());


        return cloneTV;
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
