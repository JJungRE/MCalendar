package com.roopre.mcalendar;

/**
 * Created by munjongmin on 2017. 10. 22..
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

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
        Log.d(TAG, "parent Height = " + Height);
        Log.d(TAG, "parent Width = " + Width);

        LinearLayout default_ll, over_ll;
        default_ll = (LinearLayout) convertView.findViewById(R.id.default_ll);
        over_ll = (LinearLayout) convertView.findViewById(R.id.over_ll);

        if(!Se_Application.strNotNull(day.get(position).getTotal())){
            default_ll.setVisibility(View.GONE);
            over_ll.setVisibility(View.GONE);
        }
        else if(Integer.parseInt(day.get(position).getTotal())<=2){
            default_ll.setVisibility(VISIBLE);
            over_ll.setVisibility(View.GONE);
        }else if(Integer.parseInt(day.get(position).getTotal())>2){
            default_ll.setVisibility(View.GONE);
            over_ll.setVisibility(VISIBLE);

            TextView total_tv = (TextView)over_ll.findViewById(R.id.total_tv);
            total_tv.setText(day.get(position).getTotal());
        }
        final LinearLayout day_linear = (LinearLayout) convertView.findViewById(R.id.day_linear);
        day_linear.setTag(day.get(position).getDay());
        AbsListView.LayoutParams tvparams = (AbsListView.LayoutParams) day_linear.getLayoutParams();
        final TextView day_tv = (TextView) convertView.findViewById(R.id.day_tv);
        if (day.size() <= 28) {
            tvparams = new AbsListView.LayoutParams(Width /7, Height / 4);
        } else if (day.size() > 28 && day.size() <= 35) {
            tvparams = new AbsListView.LayoutParams(Width/7, Height / 5);
        } else if (day.size() > 35) {
            tvparams = new AbsListView.LayoutParams(Width/7, Height / 6);
        }


        Log.d(TAG, "day_linear Width =" + day_linear.getWidth());
        day_linear.setLayoutParams(tvparams);
        if ((position + 1) % 7 == 0) {
            day_linear.setBackgroundResource(R.drawable.border_left_right_bottom_line_black);
        } else {
            day_linear.setBackgroundResource(R.drawable.border_left_bottom_line_black);
        }
        if ((position + 1) % 7 == 1) {
            day_tv.setTextColor(mContext.getResources().getColor(R.color.main_red));
        } else if ((position + 1) % 7 == 0) {
            day_tv.setTextColor(mContext.getResources().getColor(R.color.main_blue));
        } else {
            day_tv.setTextColor(mContext.getResources().getColor(R.color.main_black));
        }
        Log.d(TAG, "day.get("+position+") getDay = "+day.get(position).getDay());
        day_tv.setText(day.get(position).getDay());

        Log.d(TAG, "day_tv Width = " + day_tv.getWidth());
        Log.d(TAG, "day_tv Width *7  = " + day_tv.getWidth() * 7);
        return convertView;
    }

}