package com.roopre.mcalendar;

import android.content.Intent;
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

public class ViewListActivity extends AppCompatActivity
        implements View.OnClickListener {

    private String TAG = "ViewListActivity";
    private Menu menu;

    LinearLayout skt_linear, kt_linear, lg_linear;

    String[] seqList;
    String seq;

    Bitmap bitmap;
    ImageView closeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        seq = getIntent().getStringExtra("seq");

        Log.d(TAG, "seq = " + seq);
        seq = seq.replace("[", "");
        seq = seq.replace("]", "");

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        closeImage = (ImageView) findViewById(R.id.close_image);
        closeImage.setOnClickListener(this);

        skt_linear = (LinearLayout) findViewById(R.id.skt_linear);
        kt_linear = (LinearLayout) findViewById(R.id.kt_linear);
        lg_linear = (LinearLayout) findViewById(R.id.lg_linear);
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

    private void DrawData(String result) {
        try {

            LayoutInflater inflater = LayoutInflater.from(this);
            ImageView imageView;
            int sub_margin = getResources().getDimensionPixelSize(R.dimen.activity_half_horizontal_margin);
            int innerPadding = getResources().getDimensionPixelSize(R.dimen.activity_quarter_horizontal_margin);
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            LinearLayout.LayoutParams imageparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageparams.setMargins(sub_margin, sub_margin, sub_margin, sub_margin);
            String logo_img = null;

            skt_linear.removeAllViews();
            kt_linear.removeAllViews();
            lg_linear.removeAllViews();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                imageView = new ImageView(this);
                imageView.setTag(jsonObject.getString("seq"));
                imageView.setOnClickListener(new TextView.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewListActivity.this, ViewDetailActivity.class);
                        intent.putExtra("seq", v.getTag().toString());
                        startActivity(intent);
                    }
                });

                logo_img = jsonObject.getString("logo_img");
                int resID = getResources().getIdentifier(logo_img, "drawable", "com.roopre.mcalendar");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
                imageView.setImageBitmap(bitmap);
                imageView.setLayoutParams(imageparams);
                imageView.setAdjustViewBounds(true);
                imageView
                imageView.setPadding(innerPadding,innerPadding, innerPadding, innerPadding);
                imageView.setBackgroundResource(R.drawable.border_all_line_black);

                if (jsonObject.getString("company").equals("SKT")) {
                    skt_linear.addView(imageView);
                } else if (jsonObject.getString("company").equals("KT")) {
                    kt_linear.addView(imageView);
                } else if(jsonObject.getString("company").equals("LG")){
                    lg_linear.addView(imageView);
                }

            }
        } catch (Exception e) {
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


        switch (view.getId()) {
            case R.id.close_image:
                finish();
        }
    }
}
