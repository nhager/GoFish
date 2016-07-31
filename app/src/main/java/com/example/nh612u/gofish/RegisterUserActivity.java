package com.example.nh612u.gofish;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterUserActivity extends AppCompatActivity {

    private static final String DEFAULT_ROLE = "VETERAN";

    private String email = null;
    private String password = null;
    private Button registerButton = null;
    private Button nextButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_1);
        nextButton = (Button) findViewById(R.id.nextButton);
        setNextButtonOnClickListener();
    }

    private void setNextButtonOnClickListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tempEmail = (EditText) findViewById(R.id.registerUserEmail);
                EditText tempPassword = (EditText) findViewById(R.id.registerUserPassword);
                email = tempEmail != null ? tempEmail.getText().toString() : null;
                password = tempPassword != null ? tempPassword.getText().toString() : null;
                if (email == null || email.equals("") || password == null || password.equals("")) return;
                HttpHelper httpHelper = new HttpHelper(getNextRegisterViewCallback());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("email", email);
                    httpHelper.GET(HttpHelper.TABLE.USER, jsonObject);
                } catch (JSONException e) {
                    Log.wtf("Error:", "JSON Exception thrown");
                }
            }
        });
    }

    private Handler.Callback getNextRegisterViewCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response != null && response.contains("not found")) {
                    setNextView();
                }
                return true;
            }
        };
        return callback;
    }

    private void setNextView() {
        setContentView(R.layout.activity_register_user_2);
        registerButton = (Button) findViewById(R.id.registerUserButton);
        setRegisterButtonOnClickListener();
    }

    private void setRegisterButtonOnClickListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] information = new EditText[7];
                String[] finalInformation = new String[10];
                information[0] = ((EditText) findViewById(R.id.registerUserFirst));
                information[1] = ((EditText) findViewById(R.id.registerUserLast));
                information[2] = ((EditText) findViewById(R.id.registerUserAddress));
                information[3] = ((EditText) findViewById(R.id.registerUserCity));
                information[4] = ((EditText) findViewById(R.id.registerUserState));
                information[5] = ((EditText) findViewById(R.id.registerUserZip));
                information[6] = ((EditText) findViewById(R.id.registerUserPhone));
                for (int i = 0; i < information.length; i++) {
                    if (information[i] != null) {
                        finalInformation[i] = information[i].getText().toString();
                    }
                }
                finalInformation[7] = email;
                finalInformation[8] = password;
                finalInformation[9] = findViewById(R.id.registerUserRole) != null ?
                        ((Spinner) findViewById(R.id.registerUserRole)).getSelectedItem().toString()
                        : DEFAULT_ROLE;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("firstname", finalInformation[0]);
                    jsonObject.accumulate("lastname", finalInformation[1]);
                    jsonObject.accumulate("address", finalInformation[2]);
                    jsonObject.accumulate("city", finalInformation[3]);
                    jsonObject.accumulate("state", finalInformation[4]);
                    jsonObject.accumulate("zip", finalInformation[5]);
                    jsonObject.accumulate("phone", finalInformation[6]);
                    jsonObject.accumulate("email", finalInformation[7]);
                    jsonObject.accumulate("password", finalInformation[8]);
                    jsonObject.accumulate("role", finalInformation[9]);
                    HttpHelper httpHelper = new HttpHelper(getCreateUserCallback());
                    httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.USER, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler.Callback getCreateUserCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
                return true;
            }
        };
        return callback;
    }
}
