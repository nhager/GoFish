package com.example.nh612u.gofish;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button loginButton = (Button) findViewById(R.id.loginButt);
        final DBHelper mDbHelper = new DBHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String email = ((EditText)findViewById(R.id.editEmail)).getText().toString();
                    String password = ((EditText)findViewById(R.id.editPassword)).getText().toString();
                    String select = "SELECT * FROM " + DBHelper.FeedEntry.TABLE_NAME + " WHERE " +
                            DBHelper.FeedEntry.COLUMN_NAME_EMAIL + "=" + "'" + email + "'";
                    Cursor c = mDbHelper.getData(db, select);
                    c.moveToFirst();
                    if(c.getCount() == 0){

                    }else{
                        if(password.equals(c.getString(c.getColumnIndex
                                (DBHelper.FeedEntry.COLUMN_NAME_PASSWORD)))){
                            startActivity(new Intent(LoginActivity.this, Admin_Activity.class));
                        }
                    }

                }

            });
        } catch(NullPointerException e) {
            Log.wtf("Error:", "Null Pointer Exception thrown");
        }

    }

}
