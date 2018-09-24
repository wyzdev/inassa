package com.nassagroup.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.R;
import com.nassagroup.core.Signout;
import com.nassagroup.tools.LogOutTimerTask;
import com.nassagroup.tools.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.nassagroup.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;


public class HomeActivity extends AppCompatActivity {

    UserInfo userInfo;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(HomeActivity.this, SearchClientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_user_guide:
                    startActivity(new Intent(HomeActivity.this, UserGuide.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        }

    };
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchClientActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }
    private final List mBlockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP));

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mBlockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);

        timer = new Timer();
        Log.i("Main", "Home Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask(HomeActivity.this);
        timer.schedule(logoutTimeTask, 600000); //auto logout in 10 minutes
    }


    /**
     * Method that creates an option menu
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    /**
     * Method that allows the user to choose an item in the option menu
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setTitle("Déconnexion");
                progressDialog.setMessage("Patientez s'il vous plait ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                // logout
                logout(progressDialog);
                return true;
            case R.id.change_password:
                startActivity(new Intent(HomeActivity.this, ChangePasswordActivity.class));
                return true;
            case R.id.user_guide:
                startActivity(new Intent(HomeActivity.this, UserGuide.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "Home cancel timer");
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            Log.i("Main", "Home cancel timer");
            timer = null;
        }
    }


    public void logout(final ProgressDialog progressDialog) {
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<Signout> call = retrofitInterfaces.signout(userInfo.getUserId());
        call.enqueue(new Callback<Signout>() {
            @Override
            public void onResponse(Call<Signout> call, retrofit2.Response<Signout> response) {
                progressDialog.dismiss();
                Signout signout = response.body();
                if (signout.error){
                    Toast.makeText(HomeActivity.this, "Vous ne pouvez pas vous deconnecter",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                userInfo.setLoggedin(false);
                userInfo.clear();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Signout> call, Throwable t) {
                progressDialog.dismiss();

            }

        });
    }
}

