package com.example.nh612u.gofish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Handler.Callback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button loginButton = (Button) findViewById(R.id.loginButt);
        final Callback callback = getLoginCallback();
        try {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = ((EditText)findViewById(R.id.editEmail)).getText().toString();
                    password = ((EditText)findViewById(R.id.editPassword)).getText().toString();
                    HttpHelper httpHelper = new HttpHelper(callback);
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.accumulate("email", email)
                                .accumulate("password", password);
                        httpHelper.GET(HttpHelper.TABLE.USER, jsonObject);

                    } catch (JSONException e) {
                        Log.wtf("Error:", "JSON Exception thrown");
                    }
                }
            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        final Button registerButton = (Button) findViewById(R.id.registerButt);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
            }
        });
    }

    private boolean isLoginSuccessful(String response) {
        boolean retval = false;
        try {
            JSONObject jsonObj = new JSONObject(response);
            String jsonEmail = jsonObj.getString("email");
            String jsonPass = jsonObj.getString("password");
            retval = jsonEmail != null && !jsonEmail.equals("") && jsonPass != null && !jsonPass.equals("")
                    && jsonEmail.equals(email) && jsonPass.equals(password);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }

    private Callback getLoginCallback() {
        final Callback callback = new Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (isLoginSuccessful(response)) {
                    Intent intent = new Intent(getBaseContext(), Admin_Activity.class);
                    intent.putExtra("email", email);
                    startActivity(new Intent(LoginActivity.this, Admin_Activity.class));
                }
                return true;
            }
        };
        return callback;
    }
}
