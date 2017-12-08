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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

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
            LinearLayout sub_linear;
            ImageView imageView;
            TextView category_tv, title_tv, class_tv, benefit_tv, minus_tv, limit_tv, guide_tv, contact_tv;


            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            LinearLayout.LayoutParams subparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int sub_margin = getResources().getDimensionPixelSize(R.dimen.activity_half_horizontal_margin);
            subparams.setMargins(sub_margin, sub_margin, sub_margin, 0);


            main_linear.removeAllViews();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                sub_linear = (LinearLayout) inflater.inflate(R.layout.dialog_benefit, null, false);
                sub_linear.setLayoutParams(subparams);
                imageView = (ImageView) sub_linear.findViewById(R.id.imageView);
                category_tv = (TextView) sub_linear.findViewById(R.id.category_tv);
                title_tv = (TextView) sub_linear.findViewById(R.id.title_tv);
                class_tv = (TextView) sub_linear.findViewById(R.id.class_tv);
                benefit_tv = (TextView) sub_linear.findViewById(R.id.benefit_tv);
                minus_tv = (TextView) sub_linear.findViewById(R.id.minus_tv);
                limit_tv = (TextView) sub_linear.findViewById(R.id.limit_tv);
                guide_tv = (TextView) sub_linear.findViewById(R.id.guide_tv);
                contact_tv = (TextView) sub_linear.findViewById(R.id.contact_tv);


                category_tv.setText(jsonObject.getString("event_category"));
                final JSONObject finalJsonObject = jsonObject;
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(finalJsonObject.getString("logo_img"));
                            Log.d("now url: ", String.valueOf(url));
                            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                        } catch (IOException ex) {
                            ex.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start();
                mThread.join();
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