package iatollion.com.WebApi;

/**
 * Created by ypwang on 2016/8/30.
 */

import android.app.Activity;
import android.os.AsyncTask;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import iatollion.com.tw.R;


public class ApiConnect extends AsyncTask<JSONObject, Void, String> {

    public Activity activity;
    //public ProgressBar progressBar;

    HttpURLConnection conn;


    public ApiConnect( Activity _activity){

        this.activity = _activity;
        //this.progressBar = (ProgressBar)this.activity.findViewById(R.id.progressBar);

        //other initializations...

    }
    private Exception exception;



    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected String doInBackground(JSONObject... params) {

        String apiUrl = "null";
        String method = "null";

        try {
            apiUrl = params[0].getString("URL");
            method = params[0].getString("METHOD");
            params[0].remove("URL");
            params[0].remove("METHOD");
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
        Log.v("JSON",params[0].toString());
        if(apiUrl.equals("null") || method.equals("null"))
            return null;

        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("content-type","application/json");
//            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);

            if(method.equals("POST")) {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.connect();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(conn.getOutputStream()));
                writer.write(params[0].toString());
                writer.flush();
                writer.close();
            }
            else if(method.equals("GET")) {
                conn.setDoInput(true);
                conn.connect();
            }
            try {
                int responseCode=conn.getResponseCode();
                Log.v("resp", Integer.toString(responseCode));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                conn.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        //progressBar.setVisibility(View.GONE);
        //Log.i("INFO", response);
    }

    private String getQuery(List<Pair<String,String>> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String,String> pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }
        Log.v("Query params", result.toString());
        return result.toString();
    }
}