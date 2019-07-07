package com.Ultimatecode.atmlocatorfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Funality_class.DataPass;
import GetApprequest.ApiResponseGet;
import adpaterrecycler.AdptnearContent;
import controlers.all.AnalyticsApplication;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, DataPass {

    private GoogleMap mMap;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime, response, type, message, status, checkboxaccuracy, nextpage_token;
    private boolean mycheck, runonce, tryonce, checkapi, checkagain_hit, check_per1;
    private LocationManager manager;
    private RecyclerView recycler_mapnearby;
    private ProgressDialog pDialog;
    private ApiResponseGet apiResponseGet;
    private ArrayList<HashMap<String, Object>> storeinfo;
    private HashMap<String, Object> storeinfosub;
    private int radus;
    private double latitude, longitude;
    private FloatingActionButton fab_type;
    private String type_mode;
    private TextView nearbytext;
    private SearchView search;
    private AnalyticsApplication analyticsApplication;
    private Context context;
    private CoordinatorLayout coordinate_layout;
    private AdptnearContent apdatnearcontent;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = MapsActivity.this;

        activity = MapsActivity.this;

        analyticsApplication = new AnalyticsApplication();

        checkapi = false;
        nearbytext = (TextView) findViewById(R.id.nearbytext);

        type = getIntent().getExtras().getString("identify");

        setCustomActionBar();

        coordinate_layout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        tryonce = true;

        apiResponseGet = new ApiResponseGet();
        runonce = true;

        radus = (int) analyticsApplication.get_all(context, "pvalue", getString(R.string.Integer));

        radus = radus * 1000;
        checkboxaccuracy = String.valueOf(analyticsApplication.get_all(context, "checkbox", getString(R.string.String)));

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLastUpdateTime = "";
        updateValuesFromBundle(savedInstanceState);

        fab_type = (FloatingActionButton) findViewById(R.id.fab_type);



        type_mode = String.valueOf(analyticsApplication.get_all(context, "Mode", getString(R.string.String)));
        if ("driving".equals(type_mode)) {
            fab_type.setImageResource(R.drawable.ic_drive_eta_white_24dp);
        } else if ("walking".equals(type_mode)) {
            fab_type.setImageResource(R.drawable.ic_directions_walk_white_24dp);
        } else {
            fab_type.setImageResource(R.drawable.ic_drive_eta_white_24dp);
            analyticsApplication.set_all(context, "Mode", "driving", getString(R.string.String));
        }
        fab_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type_mode = String.valueOf(analyticsApplication.get_all(context, "Mode", getString(R.string.String)));
                if ("driving".equals(type_mode)) {
                    fab_type.setImageResource(R.drawable.ic_directions_walk_white_24dp);
                    analyticsApplication.set_all(context, "Mode", "walking", getString(R.string.String));
                } else if ("walking".equals(type_mode)) {
                    fab_type.setImageResource(R.drawable.ic_drive_eta_white_24dp);
                    analyticsApplication.set_all(context, "Mode", "driving", getString(R.string.String));
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mycheck = true;
        mMap.setMyLocationEnabled(true);
        recycler_mapnearby = (RecyclerView) findViewById(R.id.recycler_mapnearby);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!manager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {

            //GPS is not available show alert
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
            alertDialog.setTitle(R.string.gpssetting);
            alertDialog.setMessage(R.string.gpsenabletext);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                    finish();
                }
            });
            alertDialog.show();
        } else if (!analyticsApplication.InternetCheck(context)) {
            analyticsApplication.Diall_show(context, R.string.Alert, getString(R.string.networksenabletext));
            analyticsApplication.Shackbarall(coordinate_layout, getString(R.string.enableinternet));
        } else {

                AnalyticsApplication application = (AnalyticsApplication) getApplication();
                Tracker mTracker = application.getDefaultTracker();
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("MapActivty")
                        .setAction("Visitor")
                        .build());

                 checkapi = true;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        double lat1 = 0, lat2 = 0;
        double lon1 = 0, lon2 = 0;


        if (tryonce == true) {
            lat1 = latitude;
            lon1 = longitude;
            tryonce = false;
        } else {
            lat2 = latitude;
            lon2 = longitude;
            tryonce = true;
        }

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = ((sin(dlat / 2)) * (sin(dlat / 2))) + cos(lat1) * cos(lat2) * ((sin(dlon / 2)) * (sin(dlon / 2)));
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        double d = 6371 * c;

        if (runonce == true) {

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 2000, null);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(getString(R.string.YourStartLocation))).showInfoWindow();

            new allnearbyplace().execute();
            runonce = false;

        }
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

    @Override
    public void Data_send(String data) {
        if (data.equals("noplace")) {
            analyticsApplication.Shackbarall(coordinate_layout, getString(R.string.noplace));
        } else if (data.equals("internetfailed")) {
            analyticsApplication.Shackbarall(coordinate_layout, getString(R.string.internerfailed));
        }
    }

    @Override
    public void Hit_api(Boolean check) {
        checkagain_hit = check;
        if (checkagain_hit) {
            new allnearbyplace().execute();
        }
    }

    class allnearbyplace extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage(getString(R.string.Pleasewait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            storeinfo = new ArrayList<>();

            try {

                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=" + radus);
                googlePlacesUrl.append("&hasNextPage=true&nextPage()=true");
                googlePlacesUrl.append("&types=" + type);
                googlePlacesUrl.append("&sensor=false");
                if (checkagain_hit) {
                    googlePlacesUrl.append("&pagetoken=" + nextpage_token);
                }
                googlePlacesUrl.append("&key=" + getString(R.string.key));
                String s = googlePlacesUrl.toString();

                response = apiResponseGet.getapi(s);
                if (response != null) {

                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");

                    if ("OK".equals(status)) {
                        if (jsonObject.has("next_page_token")) {
                            nextpage_token = jsonObject.getString("next_page_token");
                        } else {
                            nextpage_token = "NoToken";
                        }

                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int in = 0; in <= jsonArray.length(); in++) {
                            JSONObject j1 = jsonArray.getJSONObject(in);
                            JSONObject j2 = j1.getJSONObject("geometry");
                            JSONObject j3 = j2.getJSONObject("location");

                            storeinfosub = new HashMap<>();
                            storeinfosub.put("name", j1.getString("name"));
                            storeinfosub.put("vicinity", j1.getString("vicinity"));
                            storeinfosub.put("lat", j3.getString("lat"));
                            storeinfosub.put("lng", j3.getString("lng"));
                            storeinfo.add(storeinfosub);
                        }
                        message = jsonObject.getString("Message");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if (response != null) {

                if ("OK".equals(status)) {
                    if (checkagain_hit) {
                        apdatnearcontent.add(storeinfo, nextpage_token);
                        apdatnearcontent.notifyDataSetChanged();

                    } else {
                        recycler_mapnearby.setLayoutManager(new LinearLayoutManager(recycler_mapnearby.getContext()));
                        apdatnearcontent = new AdptnearContent(recycler_mapnearby.getContext(), storeinfo, latitude, longitude, nextpage_token,activity);
                        recycler_mapnearby.setAdapter(apdatnearcontent);
                        search.setOnQueryTextListener(listener);
                    }
                    if ("atm".equals(type)) {
                        nearbytext.setText(R.string.nearbyatm);
                    } else {
                        nearbytext.setText(R.string.nearbybank);
                    }
                } else {
                    analyticsApplication.Shackbarall(coordinate_layout, getString(R.string.noplace));
                    if ("atm".equals(type)) {

                        nearbytext.setText(R.string.nonearbyatm);

                    } else {
                        nearbytext.setText(R.string.nonearbybank);

                    }
                }

            } else {
                if (checkapi) {
                    analyticsApplication.Shackbarall(coordinate_layout, getString(R.string.internerfailed));
                }

                if ("atm".equals(type)) {

                    nearbytext.setText(R.string.nonearbyatm);

                } else {
                    nearbytext.setText(R.string.nonearbybank);

                }

            }
        }
    }


    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            for (int i = 0; i < storeinfo.size(); i++) {

                final String text = storeinfo.get(i).toString().toLowerCase();
                if (text.contains(query)) {
                    filteredList.add(storeinfo.get(i));
                }
            }

            recycler_mapnearby.setLayoutManager(new LinearLayoutManager(recycler_mapnearby.getContext()));
            nextpage_token = "NoToken";
            apdatnearcontent = new AdptnearContent(recycler_mapnearby.getContext(), filteredList, latitude, longitude, nextpage_token,activity);
            recycler_mapnearby.setAdapter(apdatnearcontent);
            apdatnearcontent.notifyDataSetChanged();
            return true;
        }
    };

    private void admethod() {
    }


    //Custom Action Bar
    private void setCustomActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search = (SearchView) toolbar.findViewById(R.id.menu_search);
        setSupportActionBar(toolbar);
        if ("atm".equals(type)) {
            getSupportActionBar().setTitle(R.string.atmlocator);
            nearbytext.setText(R.string.nonearbyatm);
        } else {
            getSupportActionBar().setTitle(R.string.banklocator);
            nearbytext.setText(R.string.nonearbybank);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        analyticsApplication.Animation_all(context, R.anim.no_change, R.anim.slide_down_info);
        analyticsApplication.Remove_key(context,"Count");
    }

    @Override
    public void onDestroy() {
        analyticsApplication.Remove_key(context,"Count");
        super.onDestroy();

    }
}
