package com.roopre.mcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SetCompanyActivity extends AppCompatActivity
        implements View.OnClickListener {
    String company = "skt";

    private String TAG = "SetCompanyActivity";
    private int selectedNum = -1;

    Button nextBtn, prevBtn;
    ImageView sk_iv, kt_iv, lg_iv;

//    private SharedPreferences pref;  //사용할 SharedPreferences 선언
//    private final String REF_CALENDAR = "prevPage";  // 사용할 SharedPreferences 이름
//    private final String SPREF_COM = "spref_com"; // 사용할 데이터의 key값



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_company);

        sk_iv = (ImageView) findViewById(R.id.sk_iv);
        kt_iv = (ImageView) findViewById(R.id.kt_iv);
        lg_iv = (ImageView) findViewById(R.id.lg_iv);

        sk_iv.setOnClickListener(this);
        kt_iv.setOnClickListener(this);
        lg_iv.setOnClickListener(this);

//        pref = getSharedPreferences(REF_CALENDAR, MODE_PRIVATE);
//        PREF_NAME이라는 이름의 SharedPrefenreces를 불러온다.
//        String spref_id = pref.getString(SPREF_ID,"");
//        String spref_pw = pref.getString(SPREF_PW,"");
//        String spref_nick = pref.getString(SPREF_NICK,"");
//        // PREF_CONFIRM 키의 Value를 prevpage에 담는다.

        /*Intent intent = getIntent();
        userid = intent.getExtras().getString("userID");
        passwd = intent.getExtras().getString("userPassword");
        nickname = intent.getExtras().getString("userNick");
*/

        nextBtn = (Button)findViewById(R.id.company_next_bt);
        prevBtn = (Button)findViewById(R.id.company_back_bt);

        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {
        //눌린값을 변화시키고 Num값 부여
        switch (view.getId()){
            case R.id.sk_iv:
                company = "skt";
                selectedNum =1;
                Se_Application.company = company;
                Log.d(TAG, "Se_Applicaton company = "+Se_Application.company);
                sk_iv.setBackgroundResource(R.drawable.bg_company_selected);
                kt_iv.setBackgroundResource(R.drawable.bg_company_deselected);
                lg_iv.setBackgroundResource(R.drawable.bg_company_deselected);
                break;
            case R.id.kt_iv:
                selectedNum =2;
                company = "kt";
                Se_Application.company = company;
                Log.d(TAG, "Se_Applicaton company = "+Se_Application.company);
                sk_iv.setBackgroundResource(R.drawable.bg_company_deselected);
                kt_iv.setBackgroundResource(R.drawable.bg_company_selected);
                lg_iv.setBackgroundResource(R.drawable.bg_company_deselected);
                break;
            case R.id.lg_iv:
                selectedNum =3;
                company = "lg";
                Se_Application.company = company;
                Log.d(TAG, "Se_Applicaton company = "+Se_Application.company);
                sk_iv.setBackgroundResource(R.drawable.bg_company_deselected);
                kt_iv.setBackgroundResource(R.drawable.bg_company_deselected);
                lg_iv.setBackgroundResource(R.drawable.bg_company_selected);
                break;
            case R.id.company_next_bt:
                Se_Application.company = company;
                Intent intent = new Intent(SetCompanyActivity.this, SetClassActivity.class);
                SetCompanyActivity.this.startActivity(intent);
                SetCompanyActivity.this.finish();
                break;
            case R.id.company_back_bt:
                intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


}
