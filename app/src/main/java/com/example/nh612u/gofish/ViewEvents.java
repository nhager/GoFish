package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ViewEvents extends AppCompatActivity {

    private Button refreshViewEvents = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        refreshViewEvents = (Button) findViewById(R.id.refreshViewEvents);
        setRefreshButtonOnClickListener();
    }


    private void setRefreshButtonOnClickListener() {
        refreshViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalInformation = ((EditText) findViewById(R.id.queryUID)).getText().toString();
                finalInformation = "'" + finalInformation + "'";
                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user_id", finalInformation);

                    HttpHelper httpHelper = new HttpHelper(getViewEventCallback());
                    httpHelper.GET(HttpHelper.TABLE.EVENTS, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Handler.Callback getViewEventCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                //startActivity(new Intent(JoinEvent.this, Admin_Activity.class));

                addItems(response);

                return true;
            }
        };
        return callback;
    }


    private boolean addItems(String response) {
        boolean retval = false;
        try {
            Object json = new JSONTokener(response).nextValue();
            List<String> spinnerArray = new ArrayList<>();
            final Context cur = this;
            if (json instanceof JSONObject) {
                JSONObject jsonObj = new JSONObject(response);
                if (jsonObj.has("message") &&
                        jsonObj.getString("message").contains("not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.wtf("Testing", response);
                    spinnerArray.add(jsonObj.getString("event_id") + " "
                            + " " + jsonObj.getString("event_name")
                            + " " + jsonObj.getString("event_desc")
                            + " " + jsonObj.getString("event_address")
                            + " " + jsonObj.getString("event_organizer")
                            + " " + jsonObj.getString("event_date")
                            + " " + jsonObj.getString("event_time"));
                }
            } else if (json instanceof JSONArray) {
                JSONArray jsonObj = new JSONArray(response);
                for (int i = 0; i < jsonObj.length(); i++) {
                    spinnerArray.add(jsonObj.getJSONObject(i).getString("event_id") + " "
                            + " " + jsonObj.getJSONObject(i).getString("event_name")
                            + " " + jsonObj.getJSONObject(i).getString("event_desc")
                            + " " + jsonObj.getJSONObject(i).getString("event_address")
                            + " " + jsonObj.getJSONObject(i).getString("event_organizer")
                            + " " + jsonObj.getJSONObject(i).getString("event_date")
                            + " " + jsonObj.getJSONObject(i).getString("event_time"));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    cur, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner sItems = (Spinner) findViewById(R.id.spinnerEvents);
            sItems.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }




}
