package com.example.nh612u.gofish;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;

import com.loopj.android.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Handler.Callback;
import android.telecom.Call;
import android.util.Log;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fmonteiro on 7/28/2016.
 */
public class HttpHelper {
    public enum TABLE {
        USER;
    }

    private static final String SERVER_URL = "http://go-fish-api.herokuapp.com/";
    private static HttpURLConnection connection = null;

    private AsyncHttpClient client = new AsyncHttpClient();
    private Callback callback = null;

    public HttpHelper(Callback callback) {
        this.callback = callback;
    }

    public void GET(final TABLE tableEnum, String... params) {
        final String urlString = buildURLString(tableEnum, params);

        client.get(urlString, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    if (statusCode == 200) {
                        final String response = new String(responseBody, "UTF-8");
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response);
                        message.setData(bundle);
                        callback.handleMessage(message);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private String buildURLString(final  TABLE tableEnum, String... params) {
        final String table = enumToString(tableEnum);
        String urlString = SERVER_URL + table;
        for (String param : params) {
            urlString += param + "&";
        }
        return urlString.substring(0, urlString.length() - 1);
    }

    private String enumToString(TABLE table) {
        switch (table) {
            case USER:
                return "user?";
            default:
                return null;
        }
    }
}
