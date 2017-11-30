package com.roopre.mcalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "SignupActivity";
    private EditText userID, userPassword, userPassword2, userNick;


    String userid, passwd, passwd2, nickname;
    Button signupBtn, returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        userID = (EditText) findViewById(R.id.email_et);
        userPassword = (EditText) findViewById(R.id.pw_et);
        userPassword2 = (EditText) findViewById(R.id.pw2_et);
        userNick = (EditText) findViewById(R.id.nick_et);

        signupBtn = (Button) findViewById(R.id.signup_next_bt);
        signupBtn.setOnClickListener(this);
        returnBtn = (Button) findViewById(R.id.signup_back_bt);
        returnBtn.setOnClickListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        userID.setText(Se_Application.Localdb.get_dataS("userid"));
        userPassword.setText(Se_Application.Localdb.get_dataS("passwd"));
        userPassword2.setText(Se_Application.Localdb.get_dataS("passwd"));
        userNick.setText(Se_Application.Localdb.get_dataS("nickname"));
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signup_next_bt:
                userid = userID.getText().toString();
                passwd = userPassword.getText().toString();
                passwd2 = userPassword2.getText().toString();
                nickname = userNick.getText().toString();


                if (CheckValid()) {
                    ConfirmProcess confirmProcess = new ConfirmProcess();
                    confirmProcess.execute();
                }
                break;
            case R.id.signup_back_bt:

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(intent);

                /*intent.putExtra("name", "mike");    //이거뭔지
                setResult(RESULT_OK, intent);       //이 역시도 머에 필요한지?*/

                finish();
                break;
        }
    }

    private boolean CheckValid(){
        if (userid.length() <= 8){
            Toast.makeText(SignupActivity.this, "아이디가 너무 짧습니다", Toast.LENGTH_SHORT).show();
            userID.requestFocus();
            return false;
        }else if(passwd.length() <= 6) {
            Toast.makeText(SignupActivity.this, "암호가 너무 짧습니다", Toast.LENGTH_SHORT).show();
            userPassword.requestFocus();
            return false;
        }
        else if (passwd2.length() <= 6){
            Toast.makeText(SignupActivity.this, "암호가 너무 짧습니다", Toast.LENGTH_SHORT).show();
            userPassword2.requestFocus();
            return false;
        } else if (!passwd.equals(passwd2)) {
            Log.d(TAG, passwd + "," + passwd2);
            Toast.makeText(SignupActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
            userPassword2.requestFocus();
            //강제로 문제가 있는 edittext로 보내줌
            return false;
        } else if (nickname.length() <= 2) {
            Toast.makeText(SignupActivity.this, "닉네임은 2자 이상 입니다", Toast.LENGTH_SHORT).show();
            userNick.requestFocus();
            return false;
        } else {
            return true;


        }
    }
    //아이디, 닉네임 확인하기
    class ConfirmProcess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "[onCreate] " + userid + " / " + passwd + " / " + nickname);

            if (result.equals("duplicate id")) {
                Toast.makeText(SignupActivity.this, "동일한 아이디가 있습니다", Toast.LENGTH_SHORT).show();
                userID.requestFocus();
            } else if (result.equals("duplicate nickname")) {
                Toast.makeText(SignupActivity.this, "동일한 닉네임이 있습니다", Toast.LENGTH_SHORT).show();
                userNick.requestFocus();
            } else {

                Se_Application.userid = userid;
                Se_Application.passwd = passwd;
                Se_Application.nickname = nickname;
                Intent intent = new Intent(getApplicationContext(), SetCompanyActivity.class);
                startActivity(intent);

                finish();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String tempId = null;
            String tempNick = null;

            try {
                tempId = URLEncoder.encode(userid, "UTF-8");
                tempNick = URLEncoder.encode(nickname, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String serverURL = "http://52.79.209.10/confirm.php";
            String postParameters = "?userid=" + tempId + "&nickname=" + tempNick;

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(serverURL + postParameters);
                Log.d("LOAD_CLASS_URL", url.toString());

                //php url에 접속
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //그곳에 나오는 echo를 읽어
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //그걸 읽어온 값
                String result = bufferedReader.readLine();
                //출력
                Log.d("php result", "[doInBackgroud] " + result);

                return result;

            } catch (Exception e) {
                Log.d(TAG, "SetClassData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }


    }


}



