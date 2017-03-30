package com.inassa.inassa.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.acs.audiojack.AesTrackData;
import com.acs.audiojack.AudioJackReader;
import com.acs.audiojack.DukptTrackData;
import com.acs.audiojack.Result;
import com.acs.audiojack.Status;
import com.acs.audiojack.TrackData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.inassa.inassa.R;
import com.inassa.inassa.constants.Constants;
import com.inassa.inassa.fragments.AuthByNumber;
import com.inassa.inassa.fragments.AuthBySwipe;
import com.inassa.inassa.fragments.AuthentificationFragment;
import com.inassa.inassa.fragments.InformationClientFragment;
import com.inassa.inassa.interfaces.Communicator;

public class MainActivity extends AppCompatActivity implements
        Communicator,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    FragmentManager manager;
    AuthentificationFragment authentificationFragment;
    Fragment fragment;



    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_main);

        init();


        manager = getFragmentManager();
        fragment = new AuthentificationFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, authentificationFragment);
        transaction.commit();
    }

    public void init() {

        authentificationFragment = new AuthentificationFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public void toAuthBySwipe() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (!(fragment instanceof AuthBySwipe))
            transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);
        fragment = new AuthBySwipe();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void toAuthByNumber() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (!(fragment instanceof AuthByNumber))
            transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);
        fragment = new AuthByNumber();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void toInformationClient() {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        fragment = new InformationClientFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    @Override
    public void toAuthentification() {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit);
        fragment = new AuthentificationFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof AuthByNumber || fragment instanceof AuthBySwipe || fragment instanceof InformationClientFragment)
            toAuthentification();
        else
            super.onBackPressed();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Toast.makeText(MainActivity.this, "Latitude: " + currentLatitude + "\n" + "Logitude : " + currentLongitude, Toast.LENGTH_LONG).show();
            Constants.setLatitude(currentLatitude);
            Constants.setLongitude(currentLongitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
 /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(MainActivity.this, "Latitude changed to : " + currentLatitude + "\n" + "Logitude changed to : " + currentLongitude, Toast.LENGTH_LONG).show();
        Constants.setLatitude(currentLatitude);
        Constants.setLongitude(currentLongitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
