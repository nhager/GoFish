package com.example.nh612u.gofish;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by jc4101 on 7/29/2016.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private String mUserId;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    private static final long UPDATE = 10000;
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE / 2;

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("LocationServiceStart", 10);
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mUserId = intent.getStringExtra("user_id");

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            try {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                updateLocation((float) mCurrentLocation.getLatitude(), (float) mCurrentLocation.getLongitude());
                Log.i("LOCATIONSERVICE", "Latitude: " + mCurrentLocation.getLatitude() + " Longitude: " + mCurrentLocation.getLongitude());
                // TODO: API call to update table
            } catch (SecurityException se) {

            }
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateLocation((float) mCurrentLocation.getLatitude(), (float) mCurrentLocation.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.i("LOCATIONSERVICE", "Latitude: " + mCurrentLocation.getLatitude() + " Longitude: " + mCurrentLocation.getLongitude());
        } catch (SecurityException se) {

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    private void updateLocation(float latitude, float longitude) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("firstname", "Demo");
            jsonObject.accumulate("lastname", "Man");
            jsonObject.accumulate("user_id", Integer.valueOf(mUserId));
            jsonObject.accumulate("latitude", latitude);
            jsonObject.accumulate("longitude", longitude);
            HttpHelper helper = new HttpHelper(getCreateLocationCallback());
            helper.POST(LocationService.this, HttpHelper.TABLE.LOCATION, jsonObject);
        }catch (JSONException je) {
            je.printStackTrace();
        }catch ( UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
    }

    private Handler.Callback getCreateLocationCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                return true;
            }
        };
        return callback;
    }
}
