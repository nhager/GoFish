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

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        final DBHelper mDbHelper = new DBHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //mDbHelper.onUpgrade(db, 3, 4);
        //mDbHelper.onCreate(db);
        //Log.wtf("Does exist?", Long.toString(mDbHelper.addData(db, "1", "nate", "hager", "nghager@ncsu.edu")));

        final Button button = (Button) findViewById(R.id.enterButton);
        //String select = "SELECT * FROM " + DBHelper.FeedEntry.TABLE_NAME + " WHERE " +
        //        DBHelper.FeedEntry.COLUMN_NAME_FIRST + "=" + "'nate'";
        //Log.wtf("Returned name", mDbHelper.getData(db, select));
        try {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String[] information = new String[10];
                    information[0] = ((EditText) findViewById(R.id.editName)).getText().toString();
                    information[1] = ((EditText) findViewById(R.id.editLast)).getText().toString();
                    information[2] = ((EditText) findViewById(R.id.editAddress)).getText().toString();
                    information[3] = ((EditText) findViewById(R.id.editCity)).getText().toString();
                    information[4] = ((EditText) findViewById(R.id.editState)).getText().toString();
                    information[5] = ((EditText) findViewById(R.id.editZip)).getText().toString();
                    information[6] = ((EditText) findViewById(R.id.editPhone)).getText().toString();
                    information[7] = ((EditText) findViewById(R.id.editEmail)).getText().toString();
                    information[8] = ((EditText) findViewById(R.id.editPassword)).getText().toString();
                    information[9] = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
                    boolean flag = true;
                    for (int i = 0; i < 10; i++) {
                        if (information[i] == null || information[i].equals("")) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        Log.wtf("Does exist?", Long.toString(mDbHelper.addDataUser(db, information[0], information[1], information[2],
                                information[3], information[4], information[5], information[6],
                                information[7], information[8], information[9])));
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("firstname", information[0]);
                        jsonObject.accumulate("lastname", information[1]);
                        jsonObject.accumulate("address", information[2]);
                        jsonObject.accumulate("city", information[3]);
                        jsonObject.accumulate("state", information[4]);
                        jsonObject.accumulate("zip", information[5]);
                        jsonObject.accumulate("phone", information[6]);
                        jsonObject.accumulate("email", information[7]);
                        jsonObject.accumulate("password", information[8]);
                        jsonObject.accumulate("role", information[9]);
                        HttpHelper httpHelper = new HttpHelper(getCreateUserCallback());
                        httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.USER, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (java.lang.NullPointerException e){
            System.out.println("THere was an exception");
        }
    }

    private Handler.Callback getCreateUserCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                return true;
            }
        };
        return callback;
    }
}
