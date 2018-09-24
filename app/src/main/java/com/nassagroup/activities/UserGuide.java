package com.nassagroup.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;

import com.nassagroup.R;
import com.nassagroup.tools.LogOutTimerTask;
import com.nassagroup.tools.UserInfo;

import java.util.Timer;

public class UserGuide extends AppCompatActivity {

    WebView mwebview;
    UserInfo userInfo;
    Timer timer;
   // PDFViewPager pdfViewPager;
    String asset = "usermanual_v_1.pdf";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(UserGuide.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(UserGuide.this, SearchClientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_user_guide:
                    startActivity(new Intent(UserGuide.this, UserGuide.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);



        // init pdf viewpager widget
      /*  pdfViewPager = (PDFViewPager) findViewById(R.id.pdfViewPager);

        // copy asset
        CopyAsset copyAsset = new CopyAssetThreadImpl(this, new Handler());
        copyAsset.copy(asset, new File(getCacheDir(), asset).getAbsolutePath());*/

        // create pdf view pager
        //pdfViewPager = new PDFViewPager(this, "usermanual_v_1.pdf");

        ////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, SearchClientActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);


        timer = new Timer();
        Log.i("Main", "guide Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask(UserGuide.this);
        timer.schedule(logoutTimeTask, 600000); //auto logout in 10 minutes
    }

    /**
     * Method that creates an option menu
     *
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
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                userInfo.setLoggedin(false);
                userInfo.clear();
                startActivity(new Intent(UserGuide.this, LoginActivity.class));
                finish();
                return true;
            case R.id.change_password:
                startActivity(new Intent(UserGuide.this, ChangePasswordActivity.class));
                return true;
            case R.id.user_guide:
                startActivity(new Intent(UserGuide.this, UserGuide.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "guide cancel timer");
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        //((PDFPagerAdapter) pdfViewPager.getAdapter()).close();

        if (timer != null) {
            timer.cancel();
            Log.i("Main", "guide cancel timer");
            timer = null;
        }
    }
}
