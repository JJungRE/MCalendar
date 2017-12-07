package com.roopre.mcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jjungre on 2017. 12. 7..
 */

public class AllDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_detail);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Setinit();
    }

    private void Setinit(){



    }
}
