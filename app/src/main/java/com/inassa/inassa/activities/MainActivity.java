package com.inassa.inassa.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.inassa.inassa.R;
import com.inassa.inassa.fragments.AuthByNumber;
import com.inassa.inassa.fragments.AuthBySwipe;
import com.inassa.inassa.fragments.AuthentificationFragment;
import com.inassa.inassa.fragments.InformationClientFragment;
import com.inassa.inassa.interfaces.Communicator;

public class MainActivity extends AppCompatActivity implements Communicator{

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

    public void init(){
        authentificationFragment = new AuthentificationFragment();

        Toolbar toolbar =(Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void toAuthBySwipe() {
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new AuthBySwipe();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void toAuthByNumber() {
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new AuthByNumber();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void toInformationClient() {
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new InformationClientFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    @Override
    public void toAuthentification() {
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new AuthentificationFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof  AuthByNumber || fragment instanceof AuthBySwipe || fragment instanceof InformationClientFragment)
            toAuthentification();
        else
            super.onBackPressed();
    }
}
