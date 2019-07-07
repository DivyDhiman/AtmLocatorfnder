package com.Ultimatecode.atmlocatorfinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import controlers.all.AnalyticsApplication;

public class PlaceDirectionMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime,checkboxaccuracy;
    private boolean mycheck, check_per1;
    private double latitude, longitude, latdes, lngdes;
    private List<LatLng> poly;
    private ArrayList<HashMap<String, Object>> storevirtualmapinfo;
    private Context context;
    private AnalyticsApplication analyticsApplication;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction_map);
        context = PlaceDirectionMap.this;

        activity = this;

        analyticsApplication = new AnalyticsApplication();

        mLastUpdateTime = "";
        updateValuesFromBundle(savedInstanceState);

        setCustomActionBar();

        checkboxaccuracy = String.valueOf(analyticsApplication.get_all(context, "checkbox", getString(R.string.String)));

        storevirtualmapinfo = new ArrayList<>();

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");


        storevirtualmapinfo = (ArrayList<HashMap<String, Object>>) getIntent().getExtras().getSerializable("InfoData");

        latdes = Double.parseDouble(getIntent().getExtras().getString("latdes"));
        lngdes = Double.parseDouble(getIntent().getExtras().getString("lngdes"));

        poly = (List<LatLng>) getIntent().getExtras().getSerializable("Poly");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapdirection();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (analyticsApplication.InternetCheck(context)) {
            AnalyticsApplication application = (AnalyticsApplication) getApplication();
            Tracker mTracker = application.getDefaultTracker();
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("DirectionMap")
                    .setAction("Visitor")
                    .build());

        }
    }

    //on resume activity
    @Override
    public void onResume() {
        super.onResume();

        if (mycheck == true) {
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
            }
        }
    }

    //when activity goes on pause
    @Override
    protected void onPause() {

        if (mycheck == true) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
        super.onPause();
    }


    private void mapdirection() {

        LatLng latLng = new LatLng(latitude, longitude);

        LatLng latLngdes = new LatLng(latdes, lngdes);


        for (int i = 0; i < poly.size() - 1; i++) {
            LatLng src = poly.get(i);
            LatLng dest = poly.get(i + 1);
            mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude, dest.longitude))
                    .width(10).color(Color.GREEN).geodesic(true));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 2000, null);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(getString(R.string.YourStartLocation))).showInfoWindow();


        mMap.addMarker(new MarkerOptions().position(latLngdes).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(getString(R.string.Destination)).snippet(storevirtualmapinfo.get(0).get("Destination").toString())).showInfoWindow();
        mMap.setMyLocationEnabled(true);
        mycheck = true;
        buildGoogleApiClient();
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    //Update location from google fused api
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
        }
    }

    //synchronized google fused location api
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    //create location request
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        if ("one".equals(checkboxaccuracy)) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        } else if ("two".equals(checkboxaccuracy)) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        } else if ("three".equals(checkboxaccuracy)) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        } else {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }
    }

    //when activity stops
    @Override
    protected void onStop() {
        if (mycheck == true) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            check_per1 = true;
        }
        startLocationUpdates();

    }

    //check connection suspended
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    //Location update
    protected void startLocationUpdates() {
        if (check_per1 == true) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    //location update close when activity closed
    protected void stopLocationUpdates() {
        if (check_per1 == true) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    //Custom Action Bar
    private void setCustomActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Directionr);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        analyticsApplication.Animation_all(context, R.anim.no_change, R.anim.slide_down_info);
    }
}

