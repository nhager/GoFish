package com.example.nh612u.gofish;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private int mInterval = 10000;
    private Handler mHandler;
    private JSONArray mResponse;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private HashMap<String, Marker> mMarkerHashMap = new HashMap<>();


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
            if (mCurrentLocation != null) {
                Log.i("MAPACTIVITY", "Latitude: " + mCurrentLocation.getLatitude() +
                        " Longitude: " + mCurrentLocation.getLongitude());
                getLocations();
            }
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

    private void getLocations() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("latitude", mCurrentLocation.getLatitude());
            jsonObject.accumulate("longitude", mCurrentLocation.getLongitude());
            HttpHelper helper = new HttpHelper(getLocationCallback());
            helper.GET(HttpHelper.TABLE.LOCATION, jsonObject);
        }catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private void setMarkers() {
        if(mResponse != null) {
            try {
                int len = mResponse.length();
                for (int i = 0; i != len; ++i) {
                    // make markers
                    JSONObject jsonLocation = (JSONObject) mResponse.get(i);
                    String user_id = jsonLocation.get("user_id").toString();
                    String name = jsonLocation.get("firstname").toString() + " " + jsonLocation.get("lastname").toString();
                    LatLng location =  new LatLng(Double.parseDouble(jsonLocation.get("latitude").toString()),
                            Double.parseDouble(jsonLocation.get("longitude").toString()));
                    if (mMarkerHashMap.get(user_id) == null) {
                        MarkerOptions markOps = new MarkerOptions().position(location).title(name);
                        mMarkerHashMap.put(user_id, mMap.addMarker(markOps));
                    } else {
                        Marker mark = mMarkerHashMap.get(user_id);
                        mark.setPosition(location);
                    }
                }
            } catch (JSONException je) {
            }
        }
    }

    private Handler.Callback getLocationCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                try {
                    mResponse = new JSONArray(response);
                    setMarkers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("LIST OF LOCATIONS!!", response);
                return true;
            }
        };
        return callback;
    }
}
