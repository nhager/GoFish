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

public class CreateEventActivity extends AppCompatActivity {

    private String eventName = null;
    private String eventDesc = null;
    private String eventAddress = null;
    private String eventOrganizer = null;
    private String eventDate = null;
    private String eventTime = null;
    private Button createButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        createButton = (Button) findViewById(R.id.createEvent);
        setCreateButtonOnClickListener();
    }

    private void setCreateButtonOnClickListener() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] information = new EditText[6];
                String[] finalInformation = new String[6];
                information[0] = ((EditText) findViewById(R.id.eventName));
                information[1] = ((EditText) findViewById(R.id.eventDescription));
                information[2] = ((EditText) findViewById(R.id.eventAddress));
                information[3] = ((EditText) findViewById(R.id.eventOrganizer));
                information[4] = ((EditText) findViewById(R.id.eventDate));
                information[5] = ((EditText) findViewById(R.id.eventTime));
                for (int i = 0; i < information.length; i++) {
                    if (information[i] != null) {
                        finalInformation[i] = information[i].getText().toString();
                    }
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("event_name", finalInformation[0]);
                    jsonObject.accumulate("event_desc", finalInformation[1]);
                    jsonObject.accumulate("event_address", finalInformation[2]);
                    jsonObject.accumulate("event_organizer", finalInformation[3]);
                    jsonObject.accumulate("event_date", finalInformation[4]);
                    jsonObject.accumulate("event_time", finalInformation[5]);

                    HttpHelper httpHelper = new HttpHelper(getCreateEventCallback());
                    httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.EVENT, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Handler.Callback getCreateEventCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                startActivity(new Intent(CreateEventActivity.this, AdminViewActivity.class));
                return true;
            }
        };
        return callback;
    }

}
