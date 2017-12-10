package com.roopre.mcalendar;

/**
 * Created by munjongmin on 2017. 10. 22..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WeekCalendarAdapter extends BaseAdapter {

    private final String TAG = "WeekCalendarAdapter";
    private final Context mContext;
    private ArrayList<WeekDayVO> days;

    // 1
    public WeekCalendarAdapter(Context context, ArrayList<WeekDayVO> days) {
        this.mContext = context;
        this.days = days;
    }

    // 2
    @Override
    public int getCount() {
        //Log.d(TAG, "days.size = "+days.size());
        return days.size();
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
            convertView = layoutInflater.inflate(R.layout.gridview_week_child, null);
        }

        int Height = parent.getHeight();
        int Width = parent.getWidth();

        ImageView imageView1, imageView2, imageView3, imageView4;
        TextView dayTv, dayTv2, totalTv;

        imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
        imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
        imageView4 = (ImageView) convertView.findViewById(R.id.imageView4);

        dayTv = (TextView) convertView.findViewById(R.id.day_tv);
        dayTv2 = (TextView) convertView.findViewById(R.id.day_tv2);
        totalTv = (TextView) convertView.findViewById(R.id.total_tv);


        if(Integer.parseInt(days.get(position).getTotal())<1){
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            totalTv.setVisibility(View.INVISIBLE);
        }else if(Integer.parseInt(days.get(position).getTotal())==1){
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setImageBitmap(days.get(position).getImage1());
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            totalTv.setVisibility(View.INVISIBLE);
        }else if(Integer.parseInt(days.get(position).getTotal())==2){
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setImageBitmap(days.get(position).getImage1());
            imageView2.setVisibility(View.VISIBLE);
            imageView2.setImageBitmap(days.get(position).getImage2());
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            totalTv.setVisibility(View.INVISIBLE);
        }else if(Integer.parseInt(days.get(position).getTotal())==3){
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setImageBitmap(days.get(position).getImage1());
            imageView2.setVisibility(View.VISIBLE);
            imageView2.setImageBitmap(days.get(position).getImage2());
            imageView3.setVisibility(View.VISIBLE);
            imageView3.setImageBitmap(days.get(position).getImage3());
            imageView4.setVisibility(View.INVISIBLE);
            totalTv.setVisibility(View.INVISIBLE);
        }else if(Integer.parseInt(days.get(position).getTotal())==4){
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setImageBitmap(days.get(position).getImage1());
            imageView2.setVisibility(View.VISIBLE);
            imageView2.setImageBitmap(days.get(position).getImage2());
            imageView3.setVisibility(View.VISIBLE);
            imageView3.setImageBitmap(days.get(position).getImage3());
            imageView4.setVisibility(View.VISIBLE);
            imageView4.setImageBitmap(days.get(position).getImage4());
            totalTv.setVisibility(View.INVISIBLE);
        }else if(Integer.parseInt(days.get(position).getTotal())>4){
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setImageBitmap(days.get(position).getImage1());
            imageView2.setVisibility(View.VISIBLE);
            imageView2.setImageBitmap(days.get(position).getImage2());
            imageView3.setVisibility(View.VISIBLE);
            imageView3.setImageBitmap(days.get(position).getImage3());
            imageView4.setVisibility(View.VISIBLE);
            imageView4.setImageBitmap(days.get(position).getImage4());
            totalTv.setVisibility(View.VISIBLE);
            totalTv.setText(Integer.toString(Integer.parseInt(days.get(position).getTotal())-4));
        }

        AbsListView.LayoutParams tvparams = new AbsListView.LayoutParams(Width, Height / 7);

        LinearLayout day_linear = (LinearLayout) convertView.findViewById(R.id.day_linear);
        day_linear.setTag(days.get(position).getDay());
        day_linear.setLayoutParams(tvparams);
        if (position == 6) {
            day_linear.setBackgroundResource(R.drawable.border_all_line_gray);
        } else {
            day_linear.setBackgroundResource(R.drawable.border_left_top_right_line_gray);
        }
        if (position == 5) {
            dayTv.setTextColor(mContext.getResources().getColor(R.color.main_blue));
        } else if (position == 6) {
            dayTv.setTextColor(mContext.getResources().getColor(R.color.main_red));
        } else {
            dayTv.setTextColor(mContext.getResources().getColor(R.color.main_black));
        }
        //Log.d(TAG, "day.get("+position+") getDay = "+days.get(position).getDay());
        String tempDate = String.format("%04d",Integer.parseInt(days.get(position).getYear()))+String.format("%02d",Integer.parseInt(days.get(position).getMonth()))+String.format("%02d",Integer.parseInt(days.get(position).getDay()));
        dayTv.setText( String.format("%02d",Integer.parseInt(days.get(position).getMonth()))+"월 "+String.format("%02d",Integer.parseInt(days.get(position).getDay())) +"일");

        dayTv2.setText(Se_Application.getYoil(tempDate));
        return convertView;
    }

}