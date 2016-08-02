package com.example.nh612u.gofish;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


        joinButton = (Button) findViewById(R.id.joinButton);
        setJoinButtonOnClickListener();
    }


    private void setJoinButtonOnClickListener() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalInformation = ((EditText) findViewById(R.id.eventID123)).getText().toString();
                String uID = ((EditText) findViewById(R.id.userID123)).getText().toString();

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("event_id", finalInformation);
                    jsonObject.accumulate("user_id", uID);

                    HttpHelper httpHelper = new HttpHelper(getJoinEventCallback());
                    httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.EVENT_SIGNUP, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Handler.Callback getJoinEventCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                startActivity(new Intent(JoinEvent.this, Admin_Activity.class));
                return true;
            }
        };
        return callback;
    }





}
