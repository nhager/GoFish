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

public class DeleteEventActivity extends AppCompatActivity {

    private String eventValue = null;
    private Button eventDelete = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);
        eventDelete = (Button) findViewById(R.id.deleteEventBtn);
        setDeleteOnClickListener();

    }


    private void setDeleteOnClickListener() {
        eventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalInformation;

                if ( ((EditText) findViewById(R.id.deleteByID)).getText().toString() != ""){
                    finalInformation =  ((EditText) findViewById(R.id.deleteByID)).getText().toString();
                }
                else {
                    finalInformation =  ((EditText) findViewById(R.id.deleteByName)).getText().toString();
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    if ( ((EditText) findViewById(R.id.deleteByID)).getText().toString() != ""){
                        jsonObject.accumulate("event_id", finalInformation);
                    }
                    else {
                        jsonObject.accumulate("event_name", finalInformation);
                    }
                    HttpHelper httpHelper = new HttpHelper(getDeleteEventCallback());
                    httpHelper.DELETE(getApplicationContext(), HttpHelper.TABLE.EVENT, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Handler.Callback getDeleteEventCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                startActivity(new Intent(DeleteEventActivity.this, Admin_Activity.class));
                return true;
            }
        };
        return callback;
    }



}
