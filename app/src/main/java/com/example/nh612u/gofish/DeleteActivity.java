package com.example.nh612u.gofish;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeleteActivity extends AppCompatActivity {
    int selected =  -1;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        HttpHelper httpHelper = new HttpHelper(getSearchCallback());
        JSONObject jsonObject = new JSONObject();
        httpHelper.GET(HttpHelper.TABLE.USERS, jsonObject);
        delete = (Button) findViewById(R.id.deleteUserButton);
        setDeleteOnClick();
    }
    private void setDeleteOnClick() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listItems = (ListView) findViewById(R.id.deleteList);
                String temp = (String)listItems.getItemAtPosition(selected);
                Scanner scanner =  new Scanner(temp);
                String id = scanner.next();
                id = id.trim();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user_id", id);
                    HttpHelper httpHelper = new HttpHelper(getDeleteUserCallback());
                    httpHelper.DELETE(getApplicationContext(), HttpHelper.TABLE.USER, jsonObject);
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
                        jsonObj.get("message").equals("User with provided parameters not found.")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    spinnerArray.add(jsonObj.getString("user_id") + " "
                            + jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_FIRST) + " "
                            + jsonObj.getString(DBHelper.FeedEntry.COLUMN_NAME_LAST));
                }
            } else if (json instanceof JSONArray){
                JSONArray jsonObj = new JSONArray(response);
                for(int i = 0; i < jsonObj.length(); i++){
                    spinnerArray.add(jsonObj.getJSONObject(i).getString("user_id") + " " +
                            jsonObj.getJSONObject(i).getString(DBHelper.FeedEntry.COLUMN_NAME_FIRST)
                            + " "+ jsonObj.getJSONObject(i).
                            getString(DBHelper.FeedEntry.COLUMN_NAME_LAST));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    cur, android.R.layout.simple_list_item_1, spinnerArray);
            ListView listItems = (ListView) findViewById(R.id.deleteList);
            listItems.setAdapter(adapter);
            listItems.setOnItemClickListener(new ListView.OnItemClickListener()  {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                    for(int k = 0; k < a.getChildCount(); k++){
                        a.getChildAt(k).setBackgroundColor(Color.WHITE);
                    }
                    v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    selected = i;
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
    private Handler.Callback getDeleteUserCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response);
                    if(jsonObj.get("message").equals("Deleted user.")){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "User deleted.", Toast.LENGTH_SHORT);
                        toast.show();
                        onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        };
        return callback;
    }
}
