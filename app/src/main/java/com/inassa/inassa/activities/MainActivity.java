package com.inassa.inassa.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.inassa.inassa.R;
import com.inassa.inassa.fragments.AuthByNumber;
import com.inassa.inassa.fragments.InformationClientFragment;
import com.inassa.inassa.interfaces.Communicator;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager manager;
    AuthByNumber AuthByNumber;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_main);

        init();


        manager = getFragmentManager();
        fragment = new AuthByNumber();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, AuthByNumber);
        transaction.commit();
    }

    public void init() {

        AuthByNumber = new AuthByNumber();

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
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
        fragment = new AuthByNumber();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof AuthByNumber || fragment instanceof InformationClientFragment)
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
