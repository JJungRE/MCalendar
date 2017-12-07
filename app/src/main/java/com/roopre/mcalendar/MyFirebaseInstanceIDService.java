package com.roopre.mcalendar;

/**
 * Created by jjungre on 2017. 10. 30..
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
        Se_Application.Localdb.set_dataS("token", refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "send Server");
    }
}
