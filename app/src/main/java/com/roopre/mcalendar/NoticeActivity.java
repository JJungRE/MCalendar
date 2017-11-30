package com.roopre.mcalendar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

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

        //actionBar 객체를 가져올 수 있다.
        ActionBar actionBar = getSupportActionBar();

//        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
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

