package com.example.nh612u.gofish;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JoinEvent extends AppCompatActivity {

    private String event_id = null;
    private String user_id = null;
    private Button joinButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);
        getAllEvents();
    }

    private void getAllEvents() {
        HttpHelper httpHelper = new HttpHelper(getGetAllEventsCallback());
        httpHelper.GET(HttpHelper.TABLE.EVENTS, new JSONObject());
    }


    private Handler.Callback getGetAllEventsCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response == null || response.contains("error") ||
                        response.contains("message")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                }
                return true;
            }
        };
        return callback;
    }
}
