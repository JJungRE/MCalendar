package com.roopre.mcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import static com.roopre.mcalendar.R.id.join_btn;
import static com.roopre.mcalendar.R.id.login_btn;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {

    private String TAG = "LoginActivity";

    private static final int REQUEST_CODE_MAIN = 101;
    private static final int REQUEST_CODE_SIGNUP = 104;

    //버튼 에디트 텍스트 변수 선언, 여기다 하는 이유는 LoginActivity 클래스 전역에서 사용가능하도록 함
    Button loginBtn, joinBtn;
    EditText idet, pwet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.7f;

        setContentView(R.layout.login);
        final EditText idText = (EditText) findViewById(R.id.login_email_et);
        final EditText passwordText = (EditText) findViewById(R.id.login_pw_et);

        // 버튼 객체 선언+이벤트 리스너 선언. 에디트 텍스트 객체 생성
        loginBtn = (Button) findViewById(login_btn);
        joinBtn = (Button) findViewById(join_btn);
        loginBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);

        idet = (EditText) findViewById(R.id.login_email_et);
        pwet = (EditText) findViewById(R.id.login_pw_et);

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        idet.setText(Se_Application.Localdb.get_dataS("userid"));
        pwet.setText(Se_Application.Localdb.get_dataS("passwd"));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case login_btn:
                //로그인 버튼 클릭
                if (CheckValid()) {
                    String server_url = "login.php";
                    HashMap<String, String> send_arg = new HashMap<String, String>();
                    send_arg.put("userid", idet.getText().toString());
                    send_arg.put("passwd", pwet.getText().toString());
                    Server_con serverCon = new Server_con(server_url, send_arg);
                    String result = serverCon.Receive_Server();
                    Log.d(TAG, result);
                    LoginProcess(result);
                }
                break;

            case join_btn:
                //회원가입 버튼 클릭
                finish();
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean CheckValid(){
        if(idet.getText().toString().length() < 1)
        {
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            idet.requestFocus();
            return false;
        }
        else if (pwet.getText().toString().length() < 1)
        {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            pwet.requestFocus();
            return false;
        }
        else
        {
            return true;
        }

    }
    private void LoginProcess(String result) {
        if (result.equals("accept")) {
            Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            Se_Application.Localdb.set_dataS("userid", idet.getText().toString());
            Se_Application.Localdb.set_dataS("passwd", pwet.getText().toString());
            Se_Application.Localdb.set_dataB("login", true);
            finish();
        } else {
            Toast.makeText(this, "일치하는 회원이 없습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
