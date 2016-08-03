package com.example.nh612u.gofish;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class MapAddMarkerActivity extends AppCompatActivity {
    private Button addMarkerButton;

    private String fishNameStr;
    private String fishTypeStr;
    private String fishDescriptionStr;

    private String event_id = "-1";
    private String user_id = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add_marker);
        setAddMarkerButtonListener();
        event_id = getIntent().getExtras().getString("event_id");
        user_id = getIntent().getExtras().getString("user_id");
        System.out.println(event_id + " " + user_id);
    }

    private void setAddMarkerButtonListener() {
        addMarkerButton = (Button) findViewById(R.id.addMarkerButton);
        addMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fishNameText = (EditText) findViewById(R.id.fishName);
                EditText fishTypeText = (EditText) findViewById(R.id.fishType);
                EditText fishDescText = (EditText) findViewById(R.id.fishDescription);
                fishNameStr = fishNameText != null ? fishNameText.getText().toString() : null;
                fishTypeStr = fishTypeText != null ? fishTypeText.getText().toString() : null;
                fishDescriptionStr = fishDescText != null ? fishDescText.getText().toString() : null;
                if (fishNameStr == null || fishNameStr.equals("")) {
                    fishNameText.setError("Must provide a name.");
                    return;
                }
                addNewMarker();
                startActivity(IntentHelper.createNewIntent(getIntent(), MapAddMarkerActivity.this,
                        MapMainActivity.class));
            }
        });
    }

    private void addNewMarker() {
        Location loc = MapMainActivity.getCoordinates();
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        latitude += randomDecimal();
        longitude += randomDecimal();
        final String coordinates = latitude + "," + longitude;
        HttpHelper httpHelper = new HttpHelper(getCreateMapMarkerCallback());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("event_id", event_id);
            jsonObject.accumulate("user_id", user_id);
            jsonObject.accumulate("title", fishNameStr);
            jsonObject.accumulate("fish_type", fishTypeStr);
            jsonObject.accumulate("fish_description", fishDescriptionStr);
            jsonObject.accumulate("coordinates", coordinates);
            httpHelper.POST(getApplicationContext(), HttpHelper.TABLE.MAP_MARKER, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private double randomDecimal() {
        Random r = new Random();
        int ret = r.nextInt(60 + 1);
        return (ret / 90000.0) *  (r.nextBoolean() ? 1 : -1);
    }

    private Handler.Callback getCreateMapMarkerCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response == null || response.contains("error")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_SHORT);
                    toast.show();
                }
                System.out.println(response);
                return true;
            }
        };
        return callback;
    }
}
