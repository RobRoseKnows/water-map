package io.robrose.hop.watermap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "MainActivity";
    public final int PERMISSION_REQUEST_LAST_LOCATION = Utility.PERMISSION_REQUEST_LAST_LOCATION;
    private final float ZOOM_LEVEL = 10;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private SupportMapFragment mMapFragment;

    private Map<String, String> data;
    //private Typeface robotoBold;

    @Bind(R.id.footer_name_textview) TextView footerNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        //footerNameTextView.setTypeface(robotoBold);
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.my_location_fab) void onClickMyLocationFab() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utility.permissionRationaleAndRequest(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    PERMISSION_REQUEST_LAST_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    @OnClick(R.id.add_point_fab) void onClickAddPointFab() {

    }

    @OnClick(R.id.footer_name_bar) void onClickFooterNameBar() {

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

        if(mGoogleApiClient.hasConnectedApi(LocationServices.API)) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utility.permissionRationaleAndRequest(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        PERMISSION_REQUEST_LAST_LOCATION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }

            LatLng home = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(home)
                    .title("You!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, ZOOM_LEVEL));
        }

        LatLng home = new LatLng(37.4184, 122.0880);
        mMap.addMarker(new MarkerOptions().position(home).title("Google!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, ZOOM_LEVEL));
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Google Play Services callback method. Fires when Google Play Services successfully connects
     * to the server.
     */
    @Override
    public void onConnected(Bundle bundle) {
        if(mGoogleApiClient.hasConnectedApi(LocationServices.API)) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utility.permissionRationaleAndRequest(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        PERMISSION_REQUEST_LAST_LOCATION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }

            LatLng home = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(home).title("You!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, ZOOM_LEVEL));
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
}
