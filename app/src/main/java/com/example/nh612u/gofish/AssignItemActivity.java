package com.example.nh612u.gofish;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AssignItemActivity extends AppCompatActivity {

    Spinner userSelect;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    String itemName = "";
    String itemType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_item_activity);
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
            /*try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("event_name", finalInformation[0]);
                jsonObject.accumulate("event_desc", finalInformation[1]);
                jsonObject.accumulate("event_address", finalInformation[2]);
                jsonObject.accumulate("event_organizer", finalInformation[3]);
                jsonObject.accumulate("event_date", finalInformation[4]);
                jsonObject.accumulate("event_time", finalInformation[5]);

                HttpHelper httpHelper = new HttpHelper(getAssignItemCallback());
                httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.ITEM, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            startActivity(new Intent(AssignItemActivity.this, InventoryActivity.class));
        } else {
            toast = Toast.makeText(this, "Please scan an item", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    /*
    private Handler.Callback getAssignItemCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                startActivity(new Intent(AssignItemActivity.this, Admin_Activity.class));
                return true;
            }
        };
        return callback;
    }*/

}
