package com.example.nh612u.gofish;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AssignItemActivity extends AppCompatActivity {

    Spinner userSelect;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    String itemName = "";
    String itemType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_item_activitu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String[] users = getUsers();
        userSelect = (Spinner) findViewById(R.id.userSelect);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, users);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSelect.setAdapter(dataAdapter);
        setSupportActionBar(toolbar);
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
                ((TextView) findViewById(R.id.itemName)).setText(itemName);
                ((TextView) findViewById(R.id.itemType)).setText(itemType);
            }
        }
    }

    //TODO: Get user list from database.
    //TODO: User user object instead of string.
    public String[] getUsers(){
        String[] strs = {"David Purcell","Phillp-a","Bilanco"};
        return strs;

    }

    //TODO: Make database call assigning item to user.
    public void assignItem(View v){
        Toast toast;
        if(itemType != "" && itemType != "") {
            String user = userSelect.getSelectedItem().toString();
            String assignItem = user + ',' + itemName + ',' + itemType;
            toast = Toast.makeText(this, assignItem, Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(AssignItemActivity.this, InventoryActivity.class));
        } else {
            toast = Toast.makeText(this, "Please scan an item", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
