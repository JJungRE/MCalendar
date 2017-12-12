package com.roopre.mcalendar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private String TAG = "NoticeActivity";


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<NoticeVO> listDataHeader, listDataHeader_date;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        final ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
        textviewTitle.setText("공지사항");
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.color.transparent);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_red)));

        setInit();

    }
    private void setInit() {
        String server_url = "load_notice.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, "now notice" + result);
        NoticeProcess(result);

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

    private void NoticeProcess(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;

            listDataHeader = new ArrayList<NoticeVO>();
            listDataChild = new HashMap<String, List<String>>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                List<String> notice = new ArrayList<String>();
                notice.add(jsonObject.getString("content"));

                NoticeVO noticeVO = new NoticeVO();

                noticeVO.setTitle(jsonObject.getString("subject"));
                noticeVO.setDate(jsonObject.getString("crt_dt"));
                listDataHeader.add(noticeVO);
                listDataChild.put(noticeVO.getTitle(), notice);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.notice_el);

        // preparing list data
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
}

