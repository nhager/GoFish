package com.example.nh612u.gofish;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateItemActivity extends AppCompatActivity {

    Spinner eventSelect;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    String itemName = "";
    String itemType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] events = getEvents();
        eventSelect = (Spinner) findViewById(R.id.eventFilterSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, events);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSelect.setAdapter(dataAdapter);

    }

    //TODO: Get user list from database.
    //TODO: User user object instead of string.
    public String[] getEvents(){
        String[] strs = {"David Purcell","Phillp-a","Bilanco"};
        return strs;

    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String lines[] = contents.split("\\r?\\n");
                itemName = lines[0];
                itemType = lines[1];
                Toast toast = Toast.makeText(this, "Added " + itemName + " to " , Toast.LENGTH_LONG);
                toast.show();
                //TODO: Call database to create item.
            }
        }
    }
}
