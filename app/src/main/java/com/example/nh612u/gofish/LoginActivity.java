package com.example.nh612u.gofish;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DBHelper mDbHelper = new DBHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //mDbHelper.onUpgrade(db, 3, 4);
       // mDbHelper.onCreate(db);
        Log.wtf("Does exist?", Integer.toString(mDbHelper.addData(db, "1", "nate", "hager", "nghager@ncsu.edu")));
        Log.wtf("Returned name", mDbHelper.getData(db));
    }
}
