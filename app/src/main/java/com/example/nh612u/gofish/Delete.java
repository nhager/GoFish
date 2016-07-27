package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Delete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        final Button button = (Button) findViewById(R.id.searchButton);
        final DBHelper mDbHelper = new DBHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final Context cur = this;
        try {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String select = "SELECT * FROM " + DBHelper.FeedEntry.TABLE_NAME + " WHERE " +
                        DBHelper.FeedEntry.COLUMN_NAME_FIRST + "=" + "'"
                        + ((EditText) findViewById(R.id.editFirst)).getText().toString() + "' " +
                        "AND " + DBHelper.FeedEntry.COLUMN_NAME_LAST + "=" + "'" +
                        ((EditText) findViewById(R.id.editLast)).getText().toString() + "'";
                    Cursor c = mDbHelper.getData(db, select);
                    List<String> spinnerArray =  new ArrayList<String>();
                    c.moveToFirst();
                    if(c.getCount() > 0){
                        spinnerArray.add(c.getString(c.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_FIRST))
                                +  " " + c.getString(c.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_LAST)));
                    }
                    while(c.moveToNext()){
                        spinnerArray.add(c.getString(c.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_FIRST))
                        + " " + c.getString(c.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_LAST)));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                           cur, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner sItems = (Spinner) findViewById(R.id.deleteSpinner);
                    sItems.setAdapter(adapter);


                }

            });
        } catch(NullPointerException e) {

        }
        final Button deleteButton = (Button) findViewById(R.id.deleteButton);
        try {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Spinner sItems = (Spinner) findViewById(R.id.deleteSpinner);
                    String selected = sItems.getSelectedItem().toString();
                    Scanner scanner =  new Scanner(selected);
                    String first = scanner.next();
                    String last = scanner.next();
                    Log.wtf("Deletion: ", Integer.toString(mDbHelper.deleteUser(db, first, last)));
                }
            });
        } catch(NullPointerException e){

        }

    }
}
