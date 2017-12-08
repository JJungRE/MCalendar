package com.roopre.mcalendar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016-12-05.
 */

public class Server_con {
    String TAG = "Server_con";
    private String Server_Con_Str;
    String RevStr;
    Bitmap RevImg;
    private int TIMEOUT_VALUE = 3000;

    public Server_con(String Server_JSP, HashMap<String, String> Server_ARG) {
        this.Server_Con_Str = Se_Application.Server_URL + Server_JSP + "?" + Arg_encodeString(Server_ARG);
    }

    public String Receive_Server() {
        try {

            Log.d(TAG, Server_Con_Str);
            RevStr = new Server_Con_ATask().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RevStr.toString();
    }

    public void Send_Server() {
        new Server_Con_ATask().execute();
    }

    public Bitmap Get_Img() {
        try {
            if (Se_Application.Network_State) {
                RevImg = new Server_Con_Img_ATask().execute().get();
            } else {
                RevImg = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_round);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RevImg;
    }

    private static String Arg_encodeString(HashMap<String, String> Arg_Map) {
        StringBuffer Arg_List = new StringBuffer(256);
        boolean first = true; // 첫 번째 매개변수 여부

        for (Map.Entry<String, String> entry : Arg_Map.entrySet()) {
            if (first) {
                first = false;
            } else {
                Arg_List.append("&");
            }
            try {
                Arg_List.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                Arg_List.append("=");
                Arg_List.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException ue) {
                ue.printStackTrace();
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Arg_List.toString();
    }

    class Server_Con_ATask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //Se_Application.isLoading(true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String Ret_Str = "";
            try {

                URL url = new URL(Server_Con_Str);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(TIMEOUT_VALUE);
                    conn.setReadTimeout(TIMEOUT_VALUE);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    int resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Ret_Str += line + "\n";
                        }
                        reader.close();
                        conn.disconnect();
                    }
                }
            } catch (SocketTimeoutException ex) {
                Ret_Str = "ERR:TIMEOUT";
                ex.printStackTrace();
            } catch (Exception ex) {
                Ret_Str = "ERR:Network Error";
                ex.printStackTrace();
            }
            return Ret_Str.trim();
        }

        protected void onPostExecute(String a) {
            //Se_Application.isLoading(false);
        }
    }

    class Server_Con_Img_ATask extends AsyncTask<String, Void, Bitmap> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... arg0) {
            Bitmap bitmap = null;

            try {
                URL url = new URL(Server_Con_Str);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(TIMEOUT_VALUE);
                    conn.setDoInput(true);

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap img) {
        }
    }
}
