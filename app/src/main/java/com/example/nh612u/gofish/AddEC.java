package com.example.nh612u.gofish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by fxedi on 8/1/2016.
 */

public class AddEC extends AppCompatActivity {
    private Button addUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        addUser = (Button) findViewById(R.id.addECButton);
        setaddECOnClickListener();
    }
    private void setaddECOnClickListener() {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) findViewById(R.id.addECFirst);
                EditText lastEditText = (EditText) findViewById(R.id.addECEmail);
                String email = nameEditText != null ? nameEditText.getText().toString() : null;
                String password = lastEditText != null ? lastEditText.getText().toString() : null;
                if (email == null || email.equals("")) {
                    nameEditText.setError("Must provide name.");
                    return;
                }
                if (password == null || password.equals("")) {
                    lastEditText.setError("Must provide email.");
                    return;
                }
                HttpHelper httpHelper = new HttpHelper(getNextRegisterViewCallback());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("email", email);
                    httpHelper.GET(HttpHelper.TABLE.EMERGENCY_CONTACT, jsonObject);
                } catch (JSONException e) {
                    Log.wtf("Error:", "JSON Exception thrown");
                }

            }
        });
    }
    private Handler.Callback getNextRegisterViewCallback() {
        return new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");

                if (response != null) {
                    if (response.contains("not found")) {
                        addEC();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Name already exists. Pick another.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Server error.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            }
        };
    }
    private void addEC(){
        String[] information = new String[8];
        information[0] = ((EditText) findViewById(R.id.addECFirst)).getText().toString();
        information[1] = ((EditText) findViewById(R.id.addECLast)).getText().toString();
        information[2] = ((EditText) findViewById(R.id.addECAddress)).getText().toString();
        information[3] = ((EditText) findViewById(R.id.addECCity)).getText().toString();
        information[4] = ((EditText) findViewById(R.id.addECState)).getText().toString();
        information[5] = ((EditText) findViewById(R.id.addECZip)).getText().toString();
        information[6] = ((EditText) findViewById(R.id.addECPhone)).getText().toString();
        information[7] = ((EditText) findViewById(R.id.addECEmail)).getText().toString();
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", Integer.valueOf(bundle.getString("id")));
                jsonObject.accumulate("firstname", information[0]);
                jsonObject.accumulate("lastname", information[1]);
                jsonObject.accumulate("address", information[2]);
                jsonObject.accumulate("city", information[3]);
                jsonObject.accumulate("state", information[4]);
                jsonObject.accumulate("zip", information[5]);
                jsonObject.accumulate("phone", information[6]);
                jsonObject.accumulate("email", information[7]);
                HttpHelper helper = new HttpHelper(getCreateECCallback());
                helper.POST(getApplicationContext(), HttpHelper.TABLE.EMERGENCY_CONTACT, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler.Callback getCreateECCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                Bundle b = getIntent().getExtras();
                if (b != null) {
                    Intent intent = new Intent(getBaseContext(), Veteran_Activity.class);
                    intent.putExtra("email", bundle.getString("email"));
                    intent.putExtra("id", bundle.getString("id"));
                    startActivity(intent);
                }
                return true;
            }
        };
        return callback;
    }
}
