package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    Spinner eventSelect;
    Spinner typeSelect;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventSelect = (Spinner) findViewById(R.id.eventFilterSpinner);
        typeSelect = (Spinner) findViewById(R.id.typeFilterSpinner);

        final Button createItemButton = (Button) findViewById(R.id.createItemButton);
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, CreateItemActivity.class));
            }
        });

        final Button inventoryButton = (Button) findViewById(R.id.assignItemButton);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryActivity.this, AssignItemActivity.class));
            }
        });

        listView = (ListView) findViewById(R.id.inventoryList);

        HttpHelper httpHelperType = new HttpHelper(getTypesCallback());
        JSONObject jsonObjectType = new JSONObject();
        httpHelperType.GET(HttpHelper.TABLE.ITEM_TYPE, jsonObjectType);

        HttpHelper httpHelperItems = new HttpHelper(getItemsCallback());
        JSONObject jsonObjectItems = new JSONObject();
        httpHelperItems.GET(HttpHelper.TABLE.ITEMS, jsonObjectItems);

        HttpHelper httpHelperEvents = new HttpHelper(getEventsCallback());
        JSONObject jsonObjectEvents = new JSONObject();
        httpHelperEvents.GET(HttpHelper.TABLE.EVENTS, jsonObjectEvents);
    }
    private Handler.Callback getTypesCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                addTypes(response);
                return true;
            }
        };
        return callback;
    }
    private boolean addTypes(String response) {
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

                    spinnerArray.add(jsonObj.getString("item_type"));
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){

                    spinnerArray.add(jsonObj.getJSONObject(i).
                            getString("item_type"));
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSelect.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }

    private Handler.Callback getItemsCallback() {
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
                    spinnerArray.add(jsonObj.getJSONObject(i).getString("item_type") + "\t" +
                            jsonObj.getJSONObject(i).getString("item_name") + " \r\nChecked Out: " +
                            (jsonObj.getJSONObject(i).getString("assigned_user_id") == "null" ? "Yes":"No"));
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, spinnerArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            listView.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
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
}