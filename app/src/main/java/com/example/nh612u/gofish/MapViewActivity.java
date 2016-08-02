package com.example.nh612u.gofish;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private int mInterval = 10000;
    private Handler mHandler;
    private double mLatitude;
    private double mLongitude;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mHandler = new Handler();
        startMapUpdate();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        stopMapUpdate();
        super.onStop();
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
        try {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null)
                Log.i("MAPACTIVITY", "Latitude: " + mCurrentLocation.getLatitude() + " Longitude: " + mCurrentLocation.getLongitude());
        } catch (SecurityException se) {
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    // TODO: Use API call to get info from DB and update admin map
}
