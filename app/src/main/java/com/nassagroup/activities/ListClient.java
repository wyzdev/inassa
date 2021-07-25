package com.nassagroup.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
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
    UserInfo userInfo;
    String hero_name;


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
        if (extras == null) {
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

        userInfo = new UserInfo(this);

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
        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);


            String primary_name = "";
            if (jsonObject.getString("primary_name") != null)
                primary_name = jsonObject.getString("primary_name");

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Recherche des bénéfices");
            progressDialog.setMessage("Patientez s'il vous plait ... ");

            /*proceedGetBenefits(
                    progressDialog,
                    jsonObject.getInt("employee_id"),
                    jsonObject.getString(FIRSTNAME),
                    jsonObject.toString()

            );*/

            clients.add(
                    new Client(
                            jsonObject.getString(FIRSTNAME),
                            jsonObject.getString(LASTNAME),
                            jsonObject.getString(DOB),
                            jsonObject.getString(COMPANY),
                            jsonObject.getString(POLICY_NUMBER),
                            jsonObject.getString(GLOBAL_NAME_NUMBER),
                            jsonObject.getString(ADDRESS).replace("\n", " "),
                            jsonObject.getBoolean(STATUS),
                            jsonObject.getInt("employee_id"),
                            (jsonObject.getLong("employee_id") != jsonObject.getLong("primary_employee_id")),
                            primary_name,
                            jsonObject.getInt("primary_employee_id"),
                            "NULL",
//                            jsonObject.getString("hero_tag"),
                            jsonObject.getString("legacy_policy_number")
                    ));
        }
    }


/*
    public void proceedGetBenefits(final ProgressDialog progressDialog, final int employee_id,
                                   final String firstname, final String info_client) {

        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClient().create(RetrofitInterfaces.class);

//        Toast.makeText(SearchClientActivity.this, key +
//                "\n" + employee_id +
//                "\n" + firstname, Toast.LENGTH_SHORT).show();

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse("{\"resquestkey\":{\"key\":\"" + userInfo.getKey() + " \"},\"employee_id\": " + employee_id + ",\"first_name\": \"" + firstname + "\"}").getAsJsonObject();

        Call<Benefits> call = retrofitInterfaces.postRawGetBenefits(o);
        call.enqueue(new Callback<Benefits>() {
            @Override
            public void onResponse(Call<Benefits> call, retrofit2.Response<Benefits> response) {
                progressDialog.dismiss();
                //Toast.makeText(SearchClientActivity.this, response.body().isSuccess() + "", Toast.LENGTH_LONG).show();
                Benefits Benefits = response.body();
                List<PolicyExtension> policyExtensions = Benefits.getPolicyExtensions();

                if (policyExtensions != null) {
                    int pos_extension_hero = posExtensionHero(policyExtensions);
                    if (pos_extension_hero != -1) {
                        hero_name = policyExtensions.get(pos_extension_hero).getNameForDisplay();
                    } else {
                        hero_name = "N/A";
                    }

                    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final Date date = new Date();

                    JSONObject obj;
                    String firstname = "";
                    String lastname = "";
                    boolean status = false;
                    String dob = "";
                    DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    try {
                        obj = new JSONObject(info_client);
                        if (obj.getBoolean("success") && obj.getJSONArray("clients").length() > 0) {

                            obj = (JSONObject) obj.getJSONArray("clients").get(0);
                            firstname = obj.getString("first_name");
                            lastname = obj.getString("last_name");
                            status = obj.getBoolean("status");
                            Date birthdate = originalFormat.parse(obj.getString("dob"));
                            dob = targetFormat.format(birthdate);


                            int status_int = (status) ? 1 : 0;
                            int is_dependant_int = (obj.getLong("primary_employee_id") != obj.getLong("employee_id")) ? 1 : 0;

                            // Toast.makeText(SearchClientActivity.this, userInfo.getUserFirstname() + " " + userInfo.getUserLastname(), Toast.LENGTH_SHORT).show();
                            proceedSaveInHistoric(info_client,
                                    firstname,
                                    lastname,
                                    userInfo.getUserFirstname() + " " + userInfo.getUserLastname(),
                                    status_int,
                                    dateFormat.format(date),
                                    dob,
                                    userInfo.getUserInstitution(),
                                    Constants.TOKEN,
                                    obj.getInt("employee_id"),
                                    is_dependant_int,
                                    hero_name,
                                    obj.getString("primary_name"),
                                    userInfo.getUserId()
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Benefits> call, Throwable t) {
                progressDialog.dismiss();

            }

        });
//        return hero_name;
    }
*/


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
