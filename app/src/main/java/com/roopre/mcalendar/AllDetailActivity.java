package com.roopre.mcalendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jjungre on 2017. 12. 7..
 */

public class AllDetailActivity extends AppCompatActivity {

    String TAG = "AllDetailActivity";
    String company, category, class_ = "";
    LinearLayout classLinear;
    LinearLayout main_linear, guide_linear;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_detail);

        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        category = intent.getStringExtra("category");
        final ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
        textviewTitle.setText(company+"-"+category);
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.color.transparent);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_red)));
        classLinear = (LinearLayout) findViewById(R.id.class_linear);
        main_linear = (LinearLayout) findViewById(R.id.main_linear);


        Setinit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void Setinit() {

        String server_url = "class_form.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("company", company);
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, "result = "+result);
        DrawClass(result);
        LoadBenefit();
    }

    private void DrawClass(String result){
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;

            int widthMargin = getResources().getDimensionPixelSize(R.dimen.activity_quarter_horizontal_margin);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                Button button = (Button) new Button(AllDetailActivity.this);
                LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                btnparams.weight = 1;
                btnparams.setMargins(widthMargin, 0,widthMargin,0);
                button.setLayoutParams(btnparams);
                button.setText(jsonObject.getString("class_"));
                button.setTag(jsonObject.getString("class_"));
                button.setSingleLine();
                button.setBackgroundResource(R.drawable.border_button_white);
                button.setTextColor(getResources().getColor(R.color.main_666666));
                button.setLines(1);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            class_ = (String) view.getTag();
                            LoadBenefit();
                        }
                    });

                classLinear.addView(button);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadBenefit(){
        for(int i=0;i<classLinear.getChildCount();i++){
            Button tempBtn = (Button) classLinear.getChildAt(i);
            tempBtn.setBackgroundResource(R.drawable.border_button_grey);
            tempBtn.setTextColor(getResources().getColor(R.color.main_666666));
            if (class_.equals(tempBtn.getText().toString())) {
                tempBtn.setBackgroundResource(R.drawable.bg_category_red);
                tempBtn.setTextColor(getResources().getColor(R.color.White));
            }
        }

        String server_url = "load_category_benefit.php";
        HashMap<String, String> send_arg = new HashMap<String, String>();
        send_arg.put("company", company);
        send_arg.put("category", category);
        send_arg.put("class", class_);
        Server_con serverCon = new Server_con(server_url, send_arg);
        String result = serverCon.Receive_Server();
        Log.d(TAG, "result = "+result);

        DrawData(result);

    }

    private void DrawData(String result){
        try{

            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout sub_linear, guide_linear;
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
                guide_linear = (LinearLayout) sub_linear.findViewById(R.id.guide_linear);
                contact_tv = (TextView) sub_linear.findViewById(R.id.contact_tv);

                category_tv.setText(jsonObject.getString("category"));
                int resID =getResources().getIdentifier(jsonObject.getString("logo_img"), "drawable", "com.roopre.mcalendar");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
                imageView.setImageBitmap(bitmap);
                title_tv.setText(jsonObject.getString("title"));
                class_tv.setText(jsonObject.getString("class"));
                benefit_tv.setText(jsonObject.getString("benefit"));
                minus_tv.setText(jsonObject.getString("minus"));
                limit_tv.setText(jsonObject.getString("benefit_limit"));

                guide_linear.setVisibility(View.GONE);
                //guide_tv.setText(jsonObject.getString("guide"));
                contact_tv.setText(jsonObject.getString("contact"));
                main_linear.addView(sub_linear);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
