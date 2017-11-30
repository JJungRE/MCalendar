package com.roopre.mcalendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jjungre on 2017. 11. 27..
 */

public class SetMainScheduleAdapter extends BaseAdapter {

    // 아이템을 세트로 담기 위한 어레이
    private ArrayList<SetMainSchedule_Item> mItems = new ArrayList<SetMainSchedule_Item>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        // set_main_schedule' Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.set_main_schedule, parent, false);
        }

        // 'listView_custom'에 정의된 위젯에 대한 참조 획득
        TextView category_sd_tv = (TextView) convertView.findViewById(R.id.category_sd_tv);
        TextView memo_sd_tv = (TextView) convertView.findViewById(R.id.memo_sd_tv);
        Button use_sd_bt = (Button) convertView.findViewById(R.id.use_sd_bt);
        Button use_sd_bt_off = (Button) convertView.findViewById(R.id.use_sd_bt_off);
        ImageView img_sd_imav = (ImageView) convertView.findViewById(R.id.img_sd_imav);


        // 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용
        SetMainSchedule_Item mMyItem = (SetMainSchedule_Item) getItem(position);

        // 각 위젯에 세팅된 아이템을 뿌려준다.
        category_sd_tv.setText(mMyItem.getCategory());
//        img_sd_imav.setImageDrawable(mMyItem.getImg());
        memo_sd_tv.setText(mMyItem.getMemo());
        img_sd_imav.setImageBitmap(mMyItem.getImg());

        Log.d("now num", mMyItem.getUsed());

        // (위젯에 대한 이벤트리스터를 지정하고 싶다면 여기에 작성하면 된다..)
        if (mMyItem.getUsed().equals("1")) {
            use_sd_bt.setVisibility(View.VISIBLE);
            use_sd_bt.setText("사용");
        } else if (mMyItem.getUsed().equals("0")) {
            use_sd_bt_off.setVisibility(View.VISIBLE);
            use_sd_bt_off.setText("사용완료");
        }


        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성
    public void addItem(String category, Bitmap img, String content, String confirm) {

        SetMainSchedule_Item mItem = new SetMainSchedule_Item();

        // SetMainSchedule_Item에 아이템을 setting한다.
        mItem.setCategory(category);
        mItem.setImg(img);
        mItem.setMemo(content);
        mItem.setUsed(confirm);

        // mItem에 SetMainSchedule_item을 추가한다.
        mItems.add(mItem);

    }

}
