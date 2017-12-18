package com.roopre.mcalendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewDetailActivity extends AppCompatActivity
        implements View.OnClickListener{

    private String TAG = "ViewDetailActivity";
    private Menu menu;

    String[] seqList;
    String seq;
    LinearLayout main_linear;

    Bitmap bitmap;
    ImageView closeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        seq = getIntent().getStringExtra("seq");

        Log.d(TAG, "seq = "+seq);
        seq = seq.replace("[","");
        seq = seq.replace("]","");
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        main_linear = (LinearLayout) findViewById(R.id.main_linear);

        closeImage = (ImageView) findViewById(R.id.close_image);
        closeImage.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SetInit();
    }

    private void SetInit() {
        String server_url = "load_selected_benefit.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("seq", seq);
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, result);

        DrawData(result);
    }

    private void DrawData(String result){
        try{

            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout sub_linear, guide_linear;
            ImageView imageView;
            TextView category_tv, title_tv, class_tv, benefit_tv, minus_tv, limit_tv, guide_tv, contact_tv, viewdetail_tv;


            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            LinearLayout.LayoutParams subparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int sub_margin = getResources().getDimensionPixelSize(R.dimen.activity_half_horizontal_margin);
            subparams.setMargins(sub_margin, sub_margin, sub_margin, 0);

            String logo_img = null;

            main_linear.removeAllViews();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                sub_linear = (LinearLayout) inflater.inflate(R.layout.dialog_benefit, null, false);
                sub_linear.setLayoutParams(subparams);
                guide_linear = (LinearLayout) sub_linear.findViewById(R.id.guide_linear);
                imageView = (ImageView) sub_linear.findViewById(R.id.imageView);
                category_tv = (TextView) sub_linear.findViewById(R.id.category_tv);
                title_tv = (TextView) sub_linear.findViewById(R.id.title_tv);
                class_tv = (TextView) sub_linear.findViewById(R.id.class_tv);
                benefit_tv = (TextView) sub_linear.findViewById(R.id.benefit_tv);
                minus_tv = (TextView) sub_linear.findViewById(R.id.minus_tv);
                limit_tv = (TextView) sub_linear.findViewById(R.id.limit_tv);
                guide_tv = (TextView) sub_linear.findViewById(R.id.guide_tv);
                contact_tv = (TextView) sub_linear.findViewById(R.id.contact_tv);
                viewdetail_tv = (TextView) sub_linear.findViewById(R.id.viewdetail_tv);
                final LinearLayout finalGuide_linear = guide_linear;
                viewdetail_tv.setOnClickListener(new TextView.OnClickListener() {
                    public void onClick(View v) {
                        if(finalGuide_linear.getVisibility() == View.VISIBLE){
                            finalGuide_linear.setVisibility(View.GONE);
                        }
                        else
                        {
                            finalGuide_linear.setVisibility(View.VISIBLE);
                        }

                    }
                });

                category_tv.setText(jsonObject.getString("event_category"));
                logo_img = jsonObject.getString("logo_img");
                int resID =getResources().getIdentifier(logo_img, "drawable", "com.roopre.mcalendar");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
                imageView.setImageBitmap(bitmap);
                title_tv.setText(jsonObject.getString("event_title"));
                class_tv.setText(jsonObject.getString("class"));
                benefit_tv.setText(jsonObject.getString("event_benefit"));
                minus_tv.setText(jsonObject.getString("event_minus"));
                limit_tv.setText(jsonObject.getString("event_limit"));
                guide_tv.setText(jsonObject.getString("event_guide"));
                contact_tv.setText(jsonObject.getString("contact"));
                main_linear.addView(sub_linear);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.close_image:
                finish();
        }
    }
}
