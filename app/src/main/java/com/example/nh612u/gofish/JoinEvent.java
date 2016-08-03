package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class JoinEvent extends AppCompatActivity {

    private String event_id = null;
    private String user_id = null;
    private Button joinButton = null;
    private int selected =  -1;
    private List<JSONObject> rows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        /*joinButton = (Button) findViewById(R.id.eventJoinButton);
        setJoinButtonOnClickListener();

        HttpHelper httpHelper = new HttpHelper(getSearchCallback());
        JSONObject jsonObject = new JSONObject();
        httpHelper.GET(HttpHelper.TABLE.EVENTS, jsonObject);
        */

        HttpHelper httpHelper = new HttpHelper(getSearchCallback());
        JSONObject jsonObject = new JSONObject();
        httpHelper.GET(HttpHelper.TABLE.EVENTS, jsonObject);
        joinButton = (Button) findViewById(R.id.eventJoinButton);
        setJoinOnClick();
    }

    private void setJoinOnClick() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listItems = (ListView) findViewById(R.id.eventJoinList);
                String temp = (String)listItems.getItemAtPosition(selected);
                Scanner scanner =  new Scanner(temp);
                String id = scanner.next();
                id = id.trim();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("event_id", id);
                    jsonObject.accumulate("user_id", getIntent().getExtras().getString("id"));
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
    private boolean addItems(String response) {
        boolean retval = false;
        try {
            Object json = new JSONTokener(response).nextValue();
            List<String> spinnerArray =  new ArrayList<>();
            final Context cur = this;
            if(json instanceof JSONObject){
                JSONObject jsonObj = new JSONObject(response);
                if(jsonObj.has("message") &&
                        jsonObj.getString("message").contains("parameters not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Event not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    spinnerArray.add(jsonObj.getString("event_id") + " "
                            + jsonObj.getString("event_name") + " "
                            + jsonObj.getString("event_desc"));
                    rows.add(jsonObj);
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){
                    spinnerArray.add(jsonObj.getJSONObject(i).getString("event_id") + " " +
                            jsonObj.getJSONObject(i).getString("event_name")
                            + " "+ jsonObj.getJSONObject(i).
                            getString("event_desc"));
                    rows.add(jsonObj.getJSONObject(i));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    cur, android.R.layout.simple_list_item_1, spinnerArray);
            ListView listItems = (ListView) findViewById(R.id.eventJoinList);
            listItems.setAdapter(adapter);
            listItems.setOnItemClickListener(new ListView.OnItemClickListener()  {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                    for(int k = 0; k < a.getChildCount(); k++){
                        a.getChildAt(k).setBackgroundColor(Color.WHITE);
                    }
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    selected = i;
                    try {
                        final JSONObject event = rows.get(selected);
                        TextView tvEventName = (TextView) findViewById(R.id.eventJoinNameDetail);
                        TextView tvEventOrganizer = (TextView) findViewById(R.id.eventJoinOrganizerDetail);
                        TextView tvEventDate = (TextView) findViewById(R.id.eventJoinDateDetail);
                        tvEventName.setText("Name: " + event.getString("event_name").toString());
                        tvEventOrganizer.setText("Organizer: " + event.getString("event_organizer").toString());
                        tvEventDate.setText("Date: " + event.getString("event_date").toString() +
                                " @ " + event.getString("event_time").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }
    private Handler.Callback getSearchCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                addItems(response);
                return true;
            }
        };
        return callback;
    }

    // need to fix...
    private Handler.Callback getJoinEventCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                System.out.println(response);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Joined Event.", Toast.LENGTH_SHORT);
                toast.show();
                onBackPressed();
                return true;
            }
        };
        return callback;
    }

    /*
    private void setJoinButtonOnClickListener() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer temp = -1;
                String id = null;
                Bundle email = getIntent().getExtras();
                if (email != null) {
                    id = email.getString("id");
                    temp = Integer.valueOf(id);
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("event_id", "3");
                    jsonObject.accumulate("user_id", temp);

                    HttpHelper httpHelper = new HttpHelper(getJoinEventCallback());
                    httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.EVENT_SIGNUP, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


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

    private Handler.Callback getSearchCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
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
            List<String> spinnerArray =  new ArrayList<>();
            final Context cur = this;
            if(json instanceof JSONObject){
                JSONObject jsonObj = new JSONObject(response);
                if(jsonObj.has("message") &&
                        jsonObj.get("message").equals("User with provided parameters not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    spinnerArray.add(jsonObj.getString("event_name") + " "
                            + jsonObj.getString("event_desc"));
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){

                    spinnerArray.add(jsonObj.getJSONObject(i).
                            getString("event_name")
                            + " "+ jsonObj.getJSONObject(i).
                            getString("event_desc"));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    cur, android.R.layout.simple_list_item_1, spinnerArray);
            ListView listItems = (ListView) findViewById(R.id.eventJoinList);
            listItems.setAdapter(adapter);
            listItems.setOnItemClickListener(new ListView.OnItemClickListener()  {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                    ListView listItems = (ListView) findViewById(R.id.eventJoinList);
                    String selected = listItems.getItemAtPosition(i).toString();
                    Scanner scanner =  new Scanner(selected);
                    String id = scanner.next();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("event_id", id);

                        Integer temp = -1;
                        String id2 = null;
                        Bundle email = getIntent().getExtras();
                        if (email != null) {
                            id2 = email.getString("id");
                            temp = Integer.valueOf(id2);
                        }
                        jsonObject.accumulate("user_id", id2);

                        HttpHelper httpHelper = new HttpHelper(getJoinEventCallback());
                        httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.EVENT_SIGNUP, jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }
    */

}
