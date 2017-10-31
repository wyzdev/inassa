package com.nassagroup.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;

import com.nassagroup.R;
import com.nassagroup.adapers.RecyclerViewAdapter;
import com.nassagroup.tools.Client;
import com.nassagroup.tools.LogOutTimerTask;
import com.nassagroup.tools.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListClient extends AppCompatActivity {

    Timer timer;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    List clients;
    private final String FIRSTNAME = "first_name";
    private final String LASTNAME = "last_name";
    private final String DOB = "dob";
    private final String ADDRESS = "address";
    private final String POLICY_NUMBER = "policy_number";
    private final String COMPANY = "company";
    private final String GLOBAL_NAME_NUMBER = "global_name_number";
    private final String CLIENTS = "clients";
    private final String STATUS = "status";
    JSONObject obj;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ListClient.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(ListClient.this, SearchClientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_user_guide:
                    startActivity(new Intent(ListClient.this, UserGuide.class));
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
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            obj = null;
        } else {
            try {
                obj = new JSONObject(extras.getString("jsonObject"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setContentView(R.layout.activity_list_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        clients = new ArrayList<>();

        try {
            fillClientArray(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, clients);
        mRecyclerView.setAdapter(adapter);

    }


    private void fillClientArray(JSONObject obj) throws JSONException {
        JSONArray jsonArray = obj.getJSONArray(CLIENTS);
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(i);
            clients.add(
                    new Client(
                            jsonObject.getString(FIRSTNAME),
                            jsonObject.getString(LASTNAME),
                            jsonObject.getString(DOB),
                            jsonObject.getString(COMPANY),
                            jsonObject.getString(POLICY_NUMBER),
                            jsonObject.getString(GLOBAL_NAME_NUMBER),
                            jsonObject.getString(ADDRESS).replace("\n", " "),
                            jsonObject.getBoolean(STATUS)
                    ));
        }
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
        Log.i("Main", "List Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask(ListClient.this);
        timer.schedule(logoutTimeTask, 600000); //auto logout in 10 minutes
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchClientActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "List cancel timer");
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            Log.i("Main", "List cancel timer");
            timer = null;
        }
    }
}
