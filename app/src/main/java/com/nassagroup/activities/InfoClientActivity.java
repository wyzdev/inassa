package com.nassagroup.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nassagroup.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A screen that displays the client's information
 */
public class InfoClientActivity extends AppCompatActivity {

    String info_client;
    JSONObject obj;
    ScrollView scrollView_info;
    TextView textView_not_update;
    LinearLayout not_update_layout;
    TextView textView_client_firstname, textView_client_lastname, textView_client_global_name_number, textView_client_status;

    private final String FIRSTNAME = "first_name";
    private final String LASTNAME = "last_name";
    private final String GLOBAL_NAME_NUMBER = "global_name_number";
    private final String CLIENTS = "clients";
    RelativeLayout viewClient, viewRecyclerView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(InfoClientActivity.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(InfoClientActivity.this, SearchClientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_user_guide:
                    startActivity(new Intent(InfoClientActivity.this, UserGuide.class));
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
        setContentView(R.layout.activity_info_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Information du client");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
/*
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/

        info_client = getIntent().getStringExtra("info_client");



        textView_client_firstname = (TextView) findViewById(R.id.info_client_firstname);
        textView_client_lastname = (TextView) findViewById(R.id.info_client_lastname);
        textView_client_global_name_number = (TextView) findViewById(R.id.info_client_global_name_number);
        textView_client_status = (TextView) findViewById(R.id.info_client_status);
        scrollView_info = (ScrollView) findViewById(R.id.scrollview_client_info);
        textView_not_update = (TextView) findViewById(R.id.client_not_update);
        not_update_layout = (LinearLayout) findViewById(R.id.not_update_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        try {
            if (!info_client.equals("")) {
                obj = new JSONObject(info_client);
                if (obj.getBoolean("success") && obj.getJSONArray(CLIENTS).length() > 0) {


                    if (obj.getJSONArray(CLIENTS).length() == 1) {
                        printInfoClient(obj);
                    }
                }
            }else{

                scrollView_info.setVisibility(View.GONE);
                textView_not_update.setVisibility(View.VISIBLE);
                not_update_layout.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfoClient(JSONObject obj) throws JSONException {
        scrollView_info.setVisibility(View.VISIBLE);
        textView_not_update.setVisibility(View.VISIBLE);
        not_update_layout.setVisibility(View.GONE);

        obj = (JSONObject) obj.getJSONArray(CLIENTS).get(0);

        textView_client_firstname.setText(obj.getString(FIRSTNAME));
        textView_client_lastname.setText(obj.getString(LASTNAME));
        textView_client_global_name_number.setText(obj.getString(GLOBAL_NAME_NUMBER));
        if (obj.getBoolean("status")) {
            textView_client_status.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_good));
            textView_client_status.setTextColor(getResources().getColor(R.color.green_active));
            textView_client_status.setText(getResources().getString(R.string.active));
        } else {
            textView_client_status.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_bad));
            textView_client_status.setTextColor(getResources().getColor(R.color.red_inactive));
            textView_client_status.setText(getResources().getString(R.string.inactive));
        }
    }

    /**
     * Method that allows the user to go to the parent activity
     *
     * @param item
     * @return MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
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
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InfoClientActivity.this, SearchClientActivity.class));
        finish();
    }
}
