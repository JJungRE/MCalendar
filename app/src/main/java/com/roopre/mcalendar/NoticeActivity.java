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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private String TAG = "NoticeActivity";


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);



        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.notice_el);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

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

        String server_url = "load_notice.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, "result = "+result);

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("세번째 공지사항 입니다.");
        listDataHeader.add("두번째 공지사항 입니다.");
        listDataHeader.add("첫번째 공지사항 입니다.");

        // Adding child data
        List<String> notice3 = new ArrayList<String>();
        notice3.add("The Shawshank Redemption");

        List<String> notice2 = new ArrayList<String>();
        notice2.add("The Wolverine");

        List<String> notice1 = new ArrayList<String>();
        notice1.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), notice3); // Header, Child data
        listDataChild.put(listDataHeader.get(1), notice2);
        listDataChild.put(listDataHeader.get(2), notice1);
    }
}

