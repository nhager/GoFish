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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AssignItemActivity extends AppCompatActivity {

    Spinner userSelect;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    String itemName = "";
    String itemType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_item_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userSelect = (Spinner) findViewById(R.id.userSelect);

        HttpHelper httpHelper = new HttpHelper(getSearchCallback());
        JSONObject jsonObject = new JSONObject();
        httpHelper.GET(HttpHelper.TABLE.USERS, jsonObject);
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
                ((TextView) findViewById(R.id.itemName)).setText(itemName);
                ((TextView) findViewById(R.id.itemType)).setText(itemType);
            }
        }
    }

    //TODO: Make database call assigning item to user.
    public void assignItem(View v){
        Toast toast;
        if(itemType != "" && itemType != "") {
            String user = userSelect.getSelectedItem().toString();
            String assignItem = user + ',' + itemName + ',' + itemType;
            toast = Toast.makeText(this, assignItem, Toast.LENGTH_LONG);
            toast.show();

            String tempUserId = "1";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("item_name", itemName);
                jsonObject.accumulate("item_type", itemType);
                jsonObject.accumulate("user_id", tempUserId);
                HttpHelper httpHelper = new HttpHelper(getAssignItemCallback());
                httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.ITEM_ASSIGN, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //startActivity(new Intent(AssignItemActivity.this, InventoryActivity.class));
        } else {
            toast = Toast.makeText(this, "Please scan an item", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private Handler.Callback getAssignItemCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                //startActivity(new Intent(AssignItemActivity.this, AdminViewActivity.class));
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

                    spinnerArray.add(jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_FIRST) + " "
                            + jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_LAST) + " ID: "
                            + jsonObj.getString("user_id"));
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){

                    spinnerArray.add(jsonObj.getJSONObject(i).
                            getString(DBHelper.FeedEntry.COLUMN_NAME_FIRST)
                            + " "+ jsonObj.getJSONObject(i).
                            getString(DBHelper.FeedEntry.COLUMN_NAME_LAST) + " ID: "
                            + jsonObj.getJSONObject(i).
                            getString("user_id"));
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            userSelect.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
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
