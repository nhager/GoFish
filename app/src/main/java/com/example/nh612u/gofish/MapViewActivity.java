package com.example.nh612u.gofish;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int mInterval = 120000;
    private Handler mHandler;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mHandler = new Handler();
        // startMapUpdate();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(39.0392,125.7625)).title("Jeff"));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker.getPosition());
        CameraUpdate updateCenter = CameraUpdateFactory.newLatLngZoom(new LatLng(39.0392,125.7625), 15.0f);;
        mMap.animateCamera(updateCenter);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                doUpdate();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startMapUpdate() {
        mStatusChecker.run();
    }

    void stopMapUpdate() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void doUpdate() {
        // update the map
    }
    // TODO: Use API call to get info from DB and update admin map
}
