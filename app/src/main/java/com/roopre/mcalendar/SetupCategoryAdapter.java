package com.roopre.mcalendar;

/**
 * Created by munjongmin on 2017. 10. 22..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class SetupCategoryAdapter extends BaseAdapter {

    private final String TAG = "SetupCategoryAdapter";
    private final Context mContext;
    private ArrayList<String> category;
    private ArrayList<String> selected;

    // 1
    public SetupCategoryAdapter(Context context, ArrayList<String> category, ArrayList<String> selected) {
        this.mContext = context;
        this.category = category;
        this.selected = selected;
    }

    // 2
    @Override
    public int getCount() {
        //Log.d(TAG, "category.size = " + category.size());
        return category.size();
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
            convertView = layoutInflater.inflate(R.layout.gridview_setup_category, null);
        }

        LinearLayout linearlayout = (LinearLayout) convertView.findViewById(R.id.linear);
        TextView title_tv = (TextView) convertView.findViewById(R.id.title_tv);
        title_tv.setText(category.get(position));

        for (int i = 0; i < selected.size(); i++) {
//            Log.d("confirm adapter select size",Integer.toString(selected.size()));

            if (selected.get(i).equals("all")) {
                linearlayout.setBackgroundResource(R.drawable.bg_category_red);

            } else {

//                if (title_tv.getText().toString().equals(selected.get(i).replace("'",""))) {
                    linearlayout.setBackgroundResource(R.drawable.border_button_grey);
//                } else {
                    //linearlayout.setBackgroundResource(R.drawable.bg_category_red);
//                }
            }
        }
        return convertView;
    }

}