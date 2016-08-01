package com.example.nh612u.gofish;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapAddMarkerActivity extends AppCompatActivity {
    private Button addMarkerButton;

    private String markerNameStr;
    private String fishNameStr;
    private String fishDescriptionStr;

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
                EditText markerNameText = (EditText) findViewById(R.id.markerName);
                EditText fishNameText = (EditText) findViewById(R.id.fishName);
                EditText fishDescText = (EditText) findViewById(R.id.fishDescription);
                markerNameStr = markerNameText != null ? markerNameText.getText().toString() : null;
                fishNameStr = fishNameText != null ? fishNameText.getText().toString() : null;
                fishDescriptionStr = fishDescText != null ? fishDescText.getText().toString() : null;
                if (markerNameStr == null || markerNameStr.equals("")) {
                    markerNameText.setError("Must provide email.");
                    return;
                }
                if (fishNameStr == null || fishNameStr.equals("")) {
                    fishNameText.setError("Must provide password.");
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
        markerOptions.title(markerNameStr);
        MapMainActivity.addMarker(markerOptions);
    }
}
