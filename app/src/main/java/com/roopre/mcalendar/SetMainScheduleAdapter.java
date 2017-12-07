package com.roopre.mcalendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jjungre on 2017. 11. 27..
 */

public class SetMainScheduleAdapter extends BaseAdapter {


    public ArrayList<SetMainSchedule_Item> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<SetMainSchedule_Item> mItems) {
        this.mItems = mItems;
    }

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

        final int pos = position;
        final Context context = parent.getContext();

        // set_main_schedule' Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.set_main_schedule, parent, false);
        }

        // 'listView_custom'에 정의된 위젯에 대한 참조 획득
        TextView num_tv = (TextView) convertView.findViewById(R.id.num_tv);
        TextView seq_tv = (TextView) convertView.findViewById(R.id.seq_tv);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView category_tv = (TextView) convertView.findViewById(R.id.category_tv);
        TextView title_tv = (TextView) convertView.findViewById(R.id.title_tv);

        // 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용
        final SetMainSchedule_Item mMyItem = (SetMainSchedule_Item) getItem(position);

        // 각 위젯에 세팅된 아이템을 뿌려준다.
        num_tv.setText(mMyItem.getNum());
        seq_tv.setText(mMyItem.getSeq());
        imageView.setImageBitmap(mMyItem.getImg());
        category_tv.setText(mMyItem.getCategory());
        title_tv.setText(mMyItem.getTitle());



        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성
    public void addItem(String num, String seq, Bitmap img, String category, String title) {
        SetMainSchedule_Item mItem = new SetMainSchedule_Item();
        // SetMainSchedule_Item에 아이템을 setting한다.
        mItem.setNum(num);
        mItem.setSeq(seq);
        mItem.setImg(img);
        mItem.setCategory(category);
        mItem.setTitle(title);
        // mItem에 SetMainSchedule_item을 추가한다.
        mItems.add(mItem);

    }



}
