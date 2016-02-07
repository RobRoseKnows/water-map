package io.robrose.hop.watermap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import com.flipboard.bottomsheet.BottomSheetLayout;
import com.amazonaws.geo.model.GeoPoint;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
//import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.robrose.hop.watermap.aws.util.WaterPin;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private static final String LOG_TAG = "MainActivity";
    public final int PERMISSION_REQUEST_LAST_LOCATION = Utility.PERMISSION_REQUEST_LAST_LOCATION;
    private final float ZOOM_LEVEL = 10;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private SupportMapFragment mMapFragment;

    // Holds all the pins by zipcode.
    private ArrayList<WaterPin> pins;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private int pinSelected = -1;
    private boolean queried = false;

    // Holds where the map is currently centered.
    private Marker focus;
    private Marker lastLocationMarker;

    @Bind(R.id.footer_name_textview) TextView footerNameTextView;

    /**
     * This method takes the current location and queries all the points within a 50km radius. It
     * then creates markers for each of the pins.
     */
    private void populateMarkers() {
        queried = true;
        getPinGroups(44.9337, -88.9761, 5000);
        for(int i = 0; i < pins.size(); i++) {
            Log.v(LOG_TAG, "Yo. Added pin.");
            WaterPin onWaterPin = pins.get(i);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(onWaterPin.lat, onWaterPin.lng))
                    .title(onWaterPin.name);
            Marker marker = mMap.addMarker(markerOptions);
            markers.add(marker);
        }
    }

    /**
     * This method queries several methods that query the DynamoDB database that stores the violation
     * data. This method takes coordinates and a radius to get points in a certain radius.
     * @param lat Latitude for center.
     * @param lng Longitude for center. // The long primitive makes variable naming weird.
     * @param radius Radius to check in meters.
     */
    private void getPinGroups(double lat, double lng, double radius) {
        FetchTests fetcher = new FetchTests(getApplicationContext());
        this.pins = (ArrayList) fetcher.radiusSearch(lat, lng, radius);
    }

    /**
     * This method refreshes the current focus of the camera to the last known location of the user.
     */
    private void refreshLastLocation() {
        LatLng home = new LatLng(44.9337, -88.9761);
        refreshLastLocation(home);
//        pinSelected = -1;
//        LatLng home = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//        MarkerOptions mark = new MarkerOptions()
//                .position(home)
//                .title("You!");
//        lastLocationMarker = mMap.addMarker(mark);
//        focus = lastLocationMarker;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark.getPosition(), ZOOM_LEVEL));
    }

    /**
     * This is the exact same as the one above but this one will take manual values.
     * @param home Latitude and longitude to set as the users last known location.
     */
    private void refreshLastLocation(LatLng home) {
        pinSelected = -1;
        MarkerOptions mark = new MarkerOptions()
                .position(home)
                .title("You!");
        lastLocationMarker = mMap.addMarker(mark);
        focus = lastLocationMarker;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark.getPosition(), ZOOM_LEVEL));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);


        //robotoBold = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

            mMapFragment.getMapAsync(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        ButterKnife.bind(this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.my_location_fab)
    public void onClickMyLocationFab(FloatingActionButton fab) {
        Log.v(LOG_TAG, "Click FAB");
//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Utility.permissionRationaleAndRequest(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    PERMISSION_REQUEST_LAST_LOCATION);
//        } else {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            refreshLastLocation();
//        }
        WaterPin wp = new WaterPin("East Capitol St NE & First St SE",
                "Washington",
                "DC",
                true,
                "3000",
                20004,
                38.8899,
                -77.0090);
        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
        intent.putExtra(Utility.BUNDLE_PIN, wp);
        startActivity(intent);
    }

    @OnClick(R.id.footer_name_bar)
    public void onClickFooterNameBar(FrameLayout frameLayout) {
        Log.v(LOG_TAG, "Footername Bar Clicked.");
        if(!focus.equals(lastLocationMarker)) {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra(Utility.BUNDLE_PIN, pins.get(pinSelected));
            startActivity(intent);
        } else {
            if(!queried)
                populateMarkers();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng home;

        if(mGoogleApiClient.hasConnectedApi(LocationServices.API)) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utility.permissionRationaleAndRequest(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        PERMISSION_REQUEST_LAST_LOCATION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }

            refreshLastLocation();
        } else {
            home = new LatLng(47.3516, -94.6118);
            refreshLastLocation(home);
            mMap.setOnMarkerClickListener(this);
        }
    }

    /**
     * Google Play Services callback method. Fires when Google Play Services successfully connects
     * to the server.
     */
    @Override
    public void onConnected(Bundle bundle) {

        // If we connect to location services, find current user location.
        if(mGoogleApiClient.hasConnectedApi(LocationServices.API)) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utility.permissionRationaleAndRequest(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        PERMISSION_REQUEST_LAST_LOCATION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                refreshLastLocation();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            // Requested permission for ACCESS_COARSE_LOCATION
            case PERMISSION_REQUEST_LAST_LOCATION: {
                try {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // TODO Instruct user on how to turn on GPS.
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        refreshLastLocation();
                    } else {
                        Utility.showDialogText(R.string.location_permission_denied, this);
                    }
                } catch (SecurityException e) {
                    Log.d(LOG_TAG, "Permission response was wrong somehow.", e);
                }

                return;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.v(LOG_TAG, marker.getId());
        if(marker.equals(focus)) {
            //TODO Figure out what it does when double click
        } else if(marker.equals(lastLocationMarker)) {
            pinSelected = -1;
            focus = marker;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM_LEVEL));
        } else {

            for(int i = 0; i < markers.size(); i++) {
                if(markers.get(i).equals(marker)) {
                    pinSelected = i;
                    focus = marker;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM_LEVEL));
                }
            }
        }
        return true;
    }

    public WaterPin getSpecificPinGroup(int i) {
        return pins.get(i);
    }

    public Location getmLastLocation() {
        if(mGoogleApiClient.hasConnectedApi(LocationServices.API)) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utility.permissionRationaleAndRequest(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        PERMISSION_REQUEST_LAST_LOCATION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        }
        return mLastLocation;
    }
}
