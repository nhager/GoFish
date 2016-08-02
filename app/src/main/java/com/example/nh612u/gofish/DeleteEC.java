package com.example.nh612u.gofish;

import android.content.Context;
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
import java.util.Scanner;

public class DeleteEC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_ec);
        final Button deleteButton = (Button) findViewById(R.id.deleteECButton);
        try {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Spinner sItems = (Spinner) findViewById(R.id.ecSpinner);
                    String selected = sItems.getSelectedItem().toString();
                    Scanner scanner = new Scanner(selected);
                    String id = scanner.next();
                    try {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.accumulate(DBHelper.FeedEntry.COLUMN_NAME_EMAIL, id);
                        HttpHelper httpHelper = new HttpHelper(getDeleteUserCallback());
                        httpHelper.DELETE(getApplicationContext(), HttpHelper.TABLE.EMERGENCY_CONTACT, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }
        final Handler.Callback callback = getSearchCallback();
        HttpHelper httpHelper = new HttpHelper(callback);
        JSONObject jsonObject = new JSONObject();

        try {
            Bundle email = getIntent().getExtras();
            if (email != null) {
                String id = email.getString("id");
                Integer temp = Integer.valueOf(id);
                jsonObject.accumulate("user_id", temp);
                httpHelper.GET(HttpHelper.TABLE.EMERGENCY_CONTACT, jsonObject);
            }

        } catch (JSONException e) {
            Log.wtf("Error:", "JSON Exception thrown");
        }
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
                        jsonObj.get("message").equals("EC with provided parameters not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    spinnerArray.add(jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_EMAIL) + " " +
                            jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_FIRST) + " "
                            + jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_LAST));
                }
            } else if (json instanceof JSONArray) {
                JSONArray jsonObj = new JSONArray(response);
                for (int i = 0; i < jsonObj.length(); i++) {
                    spinnerArray.add(jsonObj.getJSONObject(i).getString(DBHelper.FeedEntry.COLUMN_NAME_EMAIL) + " "
                            + " " + jsonObj.getJSONObject(i).getString(DBHelper.FeedEntry.COLUMN_NAME_FIRST)
                            + " " + jsonObj.getJSONObject(i).getString(DBHelper.FeedEntry.COLUMN_NAME_LAST));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    cur, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner sItems = (Spinner) findViewById(R.id.ecSpinner);
            sItems.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }
    private Handler.Callback getDeleteUserCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(response);
                    if (jsonObj.get("message").equals("Deleted user.")) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "User deleted.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
}