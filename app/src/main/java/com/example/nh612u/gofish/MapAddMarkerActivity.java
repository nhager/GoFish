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

public class MapAddMarkerActivity extends AppCompatActivity {
    private Button addMarkerButton;

    private String fishNameStr;
    private String fishTypeStr;
    private String fishDescriptionStr;

    private int user_id = 0;
    private int event_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add_marker);
        setAddMarkerButtonListener();
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
                startActivity(new Intent(MapAddMarkerActivity.this, MapMainActivity.class));
            }
        });
    }

    private void addNewMarker() {
        Location loc = MapMainActivity.getCoordinates();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(loc.getLatitude(), loc.getLongitude()));
        markerOptions.title(fishNameStr);
        markerOptions.snippet(fishTypeStr + "," + fishDescriptionStr);
        markerOptions.draggable(true);
        // MapMainActivity.addMarker(markerOptions);
        HttpHelper httpHelper = new HttpHelper(getCreateMapMarkerCallback());
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("event_id", );
            //jsonObject.accumulate("user_id", );
            jsonObject.accumulate("title", fishNameStr);
            jsonObject.accumulate("fish_type", fishTypeStr);
            jsonObject.accumulate("fish_description", fishDescriptionStr);
            jsonObject.accumulate("coordinates", markerOptions.getPosition().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler.Callback getCreateMapMarkerCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response.contains("error")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            }
        };
        return callback;
    }
}
