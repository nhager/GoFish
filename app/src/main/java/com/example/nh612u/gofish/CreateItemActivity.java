package com.example.nh612u.gofish;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreateItemActivity extends AppCompatActivity {

    Spinner eventSelect;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    String itemName = "";
    String itemType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventSelect = (Spinner) findViewById(R.id.eventFilterSpinner);

        HttpHelper httpHelperEvents = new HttpHelper(getEventsCallback());
        JSONObject jsonObjectEvents = new JSONObject();
        httpHelperEvents.GET(HttpHelper.TABLE.EVENTS, jsonObjectEvents);
    }

    private Handler.Callback getEventsCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                addEvents(response);
                return true;
            }
        };
        return callback;
    }
    private boolean addEvents(String response) {
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
                            "Types not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    spinnerArray.add(jsonObj.getString("item_name") + " type: " +
                            jsonObj.getString("item_type") + " " + " \r\nChecked Out: " +
                            jsonObj.getString("userId") == null ? "Y":"N");
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){
                    spinnerArray.add(jsonObj.getJSONObject(i).getString("event_name"));
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            eventSelect.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String lines[] = contents.split("\\r?\\n");
                itemName = lines[0];
                itemType = lines[1];
                String eventId = "1";
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("item_name", itemName);
                    jsonObject.accumulate("item_type", itemType);
                    jsonObject.accumulate("event_id", eventId);
                    HttpHelper httpHelper = new HttpHelper(postCreateItemCallback());
                    httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.ITEM, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler.Callback postCreateItemCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                return true;
            }
        };
        return callback;
    }
}
