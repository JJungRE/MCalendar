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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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

        final int pos = position;
        final Context context = parent.getContext();

        // set_main_schedule' Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.set_main_schedule, parent, false);
        }

        // 'listView_custom'에 정의된 위젯에 대한 참조 획득
        TextView num_tv = (TextView) convertView.findViewById(R.id.num_tv);
        TextView category_sd_tv = (TextView) convertView.findViewById(R.id.category_tv);
        TextView memo_sd_tv = (TextView) convertView.findViewById(R.id.memo_tv);
        final TextView schedule_seq_tv = (TextView) convertView.findViewById(R.id.seq_tv);
        Button use_sd_bt = (Button) convertView.findViewById(R.id.used_btn);
        ImageView img_sd_imav = (ImageView) convertView.findViewById(R.id.imageView);


        // 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용
        final SetMainSchedule_Item mMyItem = (SetMainSchedule_Item) getItem(position);

        // 각 위젯에 세팅된 아이템을 뿌려준다.
        num_tv.setText(mMyItem.getNum());
        category_sd_tv.setText(mMyItem.getCategory());
//        img_sd_imav.setImageDrawable(mMyItem.getImg());
        memo_sd_tv.setText(mMyItem.getMemo());
        img_sd_imav.setImageBitmap(mMyItem.getImg());
        schedule_seq_tv.setText(mMyItem.getSchedule_seq());


        Log.d("now seq", mMyItem.getSchedule_seq());

        // (위젯에 대한 이벤트리스터를 지정하고 싶다면 여기에 작성하면 된다..)
        if (mMyItem.getUsed().equals("0")) {
            use_sd_bt.setText("사용하기");
            use_sd_bt.setBackgroundResource(R.drawable.border_button_blue);
            use_sd_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, mMyItem.getSchedule_seq() +  ":schedule_seq", Toast.LENGTH_SHORT).show();
                    String schedule_seq_value = mMyItem.getSchedule_seq();
                    HashMap<String, String> send_arg = new HashMap<String, String>();
                    send_arg.put("schedule_seq", schedule_seq_value);
                    Log.d("now11", send_arg.get("schedule_seq"));

                }

            });
        } else if (mMyItem.getUsed().equals("1")) {
            use_sd_bt.setText("사용완료");
            use_sd_bt.setBackgroundResource(R.drawable.border_button_grey);
        }

        // 위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..
        // main_schedule_listview_button



        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성
    public void addItem(String num, String category, Bitmap img, String content, String confirm, String seq) {

        SetMainSchedule_Item mItem = new SetMainSchedule_Item();

        // SetMainSchedule_Item에 아이템을 setting한다.
        mItem.setNum(num);
        mItem.setCategory(category);
        mItem.setImg(img);
        mItem.setMemo(content);
        mItem.setUsed(confirm);
        mItem.setSchedule_seq(seq);

        // mItem에 SetMainSchedule_item을 추가한다.
        mItems.add(mItem);

    }



}
