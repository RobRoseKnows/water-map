package io.robrose.hop.watermap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.OnClick;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "MainActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private SupportMapFragment mMapFragment;

    public final int PERMISSION_REQUEST_INTERNET = Utility.PERMISSION_REQUEST_INTERNET;
    public final int PERMISSION_REQUEST_LAST_LOCATION = Utility.PERMISSION_REQUEST_LAST_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // RIP simple calling.
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Utility.permissionRationaleAndRequest(this,
                    Manifest.permission.INTERNET,
                    PERMISSION_REQUEST_INTERNET);
        } else {
            mMapFragment.getMapAsync(this);
        }

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
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.my_location_fab) void onClickMyLocation() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utility.permissionRationaleAndRequest(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    PERMISSION_REQUEST_LAST_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utility.permissionRationaleAndRequest(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    PERMISSION_REQUEST_LAST_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
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
            // Requested permission for INTERNET
            case PERMISSION_REQUEST_INTERNET: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapFragment.getMapAsync(this);
                } else {
                    Utility.showDialogText(R.string.internet_permission_denied, this);
                }

                return;
            }

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
