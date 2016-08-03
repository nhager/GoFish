package com.example.nh612u.gofish;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapMainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 123;

    protected static GoogleMap mMap;
    private static Location location;

    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private static List<MarkerOptions> markerList = new ArrayList<MarkerOptions>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        setUpMap();
        setUpFloatingActionButton();
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapMain);
        mapFragment.getMapAsync(this);
    }

    private void setUpFloatingActionButton() {
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.googleMapFAB);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapMainActivity.this, MapAddMarkerActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initMap(googleMap);
        initLocationManager();
        initLocationBounds();
        initMarkers();
    }

    private void initMap(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View markerView = getLayoutInflater().inflate(R.layout.custom_marker_layout, null);
                TextView tvMarkerName = (TextView) markerView.findViewById(R.id.markerTitle);
                TextView tvFishType = (TextView) markerView.findViewById(R.id.markerFishType);
                TextView tvFishDescription = (TextView)
                        markerView.findViewById(R.id.markerFishDescription);
                // Set marker title
                tvMarkerName.setText(marker.getTitle());
                // Set fish type and description
                String markerSnippet = marker.getSnippet();
                String fishType = markerSnippet.substring(0, markerSnippet.indexOf(","));
                tvFishType.setText("Type: " + fishType);
                String fishDescription = markerSnippet.substring(markerSnippet.indexOf(",") + 1);
                tvFishDescription.setText("Desc: " + fishDescription);
                // Set latitude and longitude
                LatLng latLng = marker.getPosition();
                TextView tvLatLng = (TextView) markerView.findViewById(R.id.markerLatLng);
                String coord = Math.abs(latLng.latitude) + "\u00b0" + (latLng.latitude > 0 ? "N" : "S") + " " +
                        Math.abs(latLng.longitude) + "\u00b0" + (latLng.longitude > 0 ? "E" : "W");
                tvLatLng.setText("Coord: " + coord);
                return markerView;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    private void initLocationManager() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        initLocationService();
    }

    private void initLocationBounds() {
        Location loc = getCoordinates();
        LatLngBounds locBounds = new LatLngBounds(
                new LatLng(loc.getLatitude() - 5, loc.getLongitude() - 5),
                new LatLng(loc.getLatitude() + 5, loc.getLongitude() + 5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locBounds.getCenter(), 15));
    }

    private void initMarkers() {
        for (MarkerOptions marker : markerList) {
            mMap.addMarker(marker);
        }
        HttpHelper httpHelper = new HttpHelper(getGetMapMarkerCallback());
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("event_id", getIntent().getStringExtra("event_id"));
            jsonObject.accumulate("user_id", getIntent().getStringExtra("user_id"));
            System.out.println(getIntent().getStringExtra("user_id"));
            //httpHelper.GET(HttpHelper.TABLE.MAP_MARKER, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void addMarker(MarkerOptions markerOptions) {
        markerList.add(markerOptions);
    }

    @TargetApi(23)
    private void initLocationService() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_COURSE_LOCATION);
                return;
            }
        }

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            if (locationManager != null)  {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (locationManager != null)   {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    public static Location getCoordinates() {
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        initLocationService();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    private Handler.Callback getGetMapMarkerCallback() {
        final Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                final String response = bundle.getString("response");
                if (response == null || response.contains("error") || response.contains("message")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            }
        };
        return callback;
    }
}
