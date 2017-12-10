package com.roopre.mcalendar;

/**
 * Created by munjongmin on 2017. 10. 22..
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MonthCalendarAdapter extends BaseAdapter {

    private final String TAG = "MonthCalenderAdapter";
    private final Context mContext;
    private ArrayList<MonthDayVO> day;

    // 1
    public MonthCalendarAdapter(Context context, ArrayList<MonthDayVO> day) {
        this.mContext = context;
        this.day = day;
    }

    // 2
    @Override
    public int getCount() {
        Log.d(TAG, "day.size = "+day.size());
        return day.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.gridview_month_child, null);
        }

        int Height = parent.getHeight();
        int Width = parent.getWidth();
        //Log.d(TAG, "parent Height = " + Height);
        //Log.d(TAG, "parent Width = " + Width);

        LinearLayout linear1, linear2;

        TextView textView1;
        TextView textView2;

        TextView textView3;

        linear1 = (LinearLayout) convertView.findViewById(R.id.linear1);
        linear2 = (LinearLayout) convertView.findViewById(R.id.linear2);

        textView1 = (TextView) convertView.findViewById(R.id.tv1);
        textView2 = (TextView) convertView.findViewById(R.id.tv2);
        textView3 = (TextView) convertView.findViewById(R.id.tv3);


        if(!Se_Application.strNotNull(day.get(position).getTotal()) || Integer.parseInt(day.get(position).getTotal()) == 0){
            linear1.setVisibility(View.INVISIBLE);
            linear2.setVisibility(View.GONE);
        }
        else if(Integer.parseInt(day.get(position).getTotal()) == 1){
            linear1.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView1.setText(day.get(position).getTitle1());
            textView2.setVisibility(View.INVISIBLE);
            linear2.setVisibility(View.GONE);
        }else if(Integer.parseInt(day.get(position).getTotal()) == 2){
            linear1.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView1.setText(day.get(position).getTitle1());
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(day.get(position).getTitle2());
            linear2.setVisibility(View.GONE);
        }else if(Integer.parseInt(day.get(position).getTotal()) > 2){
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);
            textView3.setText(day.get(position).getTotal());
        }

        int yoilHeight = mContext.getResources().getDimensionPixelSize(R.dimen.dp24);
        final LinearLayout day_linear = (LinearLayout) convertView.findViewById(R.id.day_linear);
        day_linear.setTag(day.get(position).getDay());
        AbsListView.LayoutParams tvparams = (AbsListView.LayoutParams) day_linear.getLayoutParams();
        final TextView day_tv = (TextView) convertView.findViewById(R.id.day_tv);
        if(position<=6){
            tvparams = new AbsListView.LayoutParams(Width /7, yoilHeight);
            //Log.d(TAG, "yoilHeight ="+yoilHeight);
        }else
        {
            if (day.size() <= 35) {
                tvparams = new AbsListView.LayoutParams(Width /7, (Height-yoilHeight) / 4);
            } else if (day.size() > 35 && day.size() <= 42) {
                tvparams = new AbsListView.LayoutParams(Width/7, (Height-yoilHeight) / 5);
            } else if (day.size() > 42) {
                tvparams = new AbsListView.LayoutParams(Width/7, (Height-yoilHeight) / 6);
            }
        }
        //Log.d(TAG, "day_linear Width =" + day_linear.getWidth());
        day_linear.setLayoutParams(tvparams);
        if(position<=5){
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.GONE);
            day_linear.setBackgroundResource(R.drawable.border_yellow_lefttopbottom_gray);
            day_linear.setGravity(Gravity.CENTER);
            day_tv.setTextColor(Color.WHITE);
        }else if(position ==6){
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.GONE);
            day_linear.setBackgroundResource(R.drawable.border_yellow_all_line_gray);
            day_linear.setGravity(Gravity.CENTER);
            day_tv.setTextColor(Color.WHITE);
        }
        else
        {
            if ((position + 1) % 7 == 0) {
                day_linear.setBackgroundResource(R.drawable.border_left_right_bottom_line_gray);
            } else {
                day_linear.setBackgroundResource(R.drawable.border_left_bottom_line_gray);
            }
        }
        if(position<=6){
            day_tv.setGravity(Gravity.CENTER);
        }else
        {
            day_tv.setGravity(Gravity.LEFT);
            if ((position + 1) % 7 == 1) {
                day_tv.setTextColor(mContext.getResources().getColor(R.color.main_red));
            } else if ((position + 1) % 7 == 0) {
                day_tv.setTextColor(mContext.getResources().getColor(R.color.main_blue));
            } else {
                day_tv.setTextColor(mContext.getResources().getColor(R.color.main_black));
            }
        }

        //Log.d(TAG, "day.get("+position+") getDay = "+day.get(position).getDay());
        day_tv.setText(day.get(position).getDay());

        //Log.d(TAG, "day_tv Width = " + day_tv.getWidth());
        //Log.d(TAG, "day_tv Width *7  = " + day_tv.getWidth() * 7);
        return convertView;
    }

}