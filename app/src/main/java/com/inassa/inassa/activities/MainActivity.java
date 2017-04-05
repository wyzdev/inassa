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

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager manager;
    AuthentificationFragment authentificationFragment;
    Fragment fragment;

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
