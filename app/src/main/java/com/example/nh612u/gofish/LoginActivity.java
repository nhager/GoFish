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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private String email;
    private String password;
    private String id;
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
                    email = ((EditText) findViewById(R.id.loginEmail)).getText().toString();
                    password = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
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
        } catch (NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

        final Button registerButton = (Button) findViewById(R.id.registerButt);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
            }
        });
    }

    private Callback getLoginCallback() {
        final Callback callback = new Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                int retVal = isLoginSuccessful(response);
                if (retVal == 1) {

                    Intent intent = new Intent(getBaseContext(), Admin_Activity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else if (retVal == 2) {
                    Intent intent = new Intent(getBaseContext(), Veteran_Activity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Email/password combination invalid.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            }
        };
        return callback;
    }

    private int isLoginSuccessful(String response) {
        int retval = -1;
        try {
            JSONObject jsonObj = new JSONObject(response);
            String jsonEmail = jsonObj.has("email") ? jsonObj.getString("email")  : null;
            String jsonPass = jsonObj.has("password") ? jsonObj.getString("password") : null;
            id = jsonObj.has("user_id") ? jsonObj.getString("user_id") : null;
            boolean isUser = jsonEmail != null && !jsonEmail.equals("") && jsonPass != null && !jsonPass.equals("")
                    && jsonEmail.equals(email) && jsonPass.equals(password);
            if(isUser){
                String role = jsonObj.has("role") ? jsonObj.getString("role") : null;
                if(role.equals("Admin")){
                    retval = 1;
                } else {
                    retval = 2;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return retval;
        }
    }
}
