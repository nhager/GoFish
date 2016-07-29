package com.example.nh612u.gofish;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

        final Callback callback = new Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (isLoginSuccessful(response)) {
                    startActivity(new Intent(LoginActivity.this, Admin_Activity.class));
                }
                return true;
            }
        };

        try {
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    email = ((EditText)findViewById(R.id.editEmail)).getText().toString();
                    password = ((EditText)findViewById(R.id.editPassword)).getText().toString();
                    HttpHelper httpHelper = new HttpHelper(callback);
                    httpHelper.GET(HttpHelper.TABLE.USER, "email=" + email, "password=" + password);
                }
            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }
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

}
