package com.nassagroup.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.R;
import com.nassagroup.RetrofitClientInstance;
import com.nassagroup.core.SendResearch;
import com.nassagroup.tools.Constants;
import com.nassagroup.tools.LogOutTimerTask;
import com.nassagroup.tools.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A screen that displays the client's information
 */
public class InfoClientActivity extends AppCompatActivity {

    TextView textView_client_name;
    TextView search_date_time;
    TextView info_client_identification;
    TextView info_client_hero;
    TextView info_client_legacy_police_number;
    TextView info_client_is_dependant;
    TextView info_client_primary_name;
    TextView info_client_company;
    Timer timer;
    String info_client;
    JSONObject obj;
    ScrollView scrollView_info;
    LinearLayout not_update_layout, dob_not_update, text_global_name_number_layout;
    TextView textView_client_firstname, textView_client_lastname, textView_client_global_name_number, textView_client_status, textView_client_dob;
    Button btn_send_mail;
    UserInfo userInfo;
    ProgressDialog progressDialog;




    private final String FIRSTNAME = "first_name";
    private final String LASTNAME = "last_name";
    private final String DOB = "dob";
    private final String GLOBAL_NAME_NUMBER = "global_name_number";
    private final String COMPANY = "company";
    private final String LEGACY_POLICE_NUMBER = "legacy_policy_number";
    //    private String hero_name = "N/A";
    private final String PRIMARY_EMPLOYEE_ID = "primary_employee_id";
    private final String PRIMARY_NAME = "primary_name";
    private final String EMPLOYEE_ID = "employee_id";
    private boolean is_dependant = false;
    private final String CLIENTS = "clients";

    LinearLayout  linear_layout_primary_name;
    LinearLayout  linear_layout_hero;
    LinearLayout  linear_layout_police_number;
    LinearLayout  linear_layout_identification;
    LinearLayout  linear_layout_dependant;
    LinearLayout  linear_layout_company;
    LinearLayout  linear_layout_client_name;
    LinearLayout info_box;
    TextView user_message;


    RelativeLayout viewClient, viewRecyclerView;
    String firstname_str, lastname_str, dob_str;

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        info_client =  extras.getString("info_client");
        userInfo = new UserInfo(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

//        Toast.makeText(this, info_client, Toast.LENGTH_SHORT).show();
//        hero_name = extras.getString("hero_name");


        if (extras.getString("first_name") != null &&  extras.getString("last_name") != null &&
                extras.getString("dob") != null) {
            firstname_str =  extras.getString("first_name");
            lastname_str =  extras.getString("last_name");
            dob_str =  extras.getString("dob");
        }

        //Toast.makeText(this, hero_name, Toast.LENGTH_SHORT).show();


        textView_client_name = (TextView) findViewById(R.id.info_client_name);
        textView_client_dob = (TextView) findViewById(R.id.info_client_dob);
        textView_client_global_name_number = (TextView) findViewById(R.id.info_client_global_name_number);
        textView_client_status = (TextView) findViewById(R.id.info_client_status);
        scrollView_info = (ScrollView) findViewById(R.id.scrollview_client_info);
        not_update_layout = (LinearLayout) findViewById(R.id.not_update_layout);
        dob_not_update = (LinearLayout) findViewById(R.id.dob_not_update);
        text_global_name_number_layout = (LinearLayout) findViewById(R.id.text_global_name_number);
        search_date_time  = (TextView) findViewById(R.id.search_date_time);
        info_client_company  = (TextView) findViewById(R.id.info_client_company);
        info_client_legacy_police_number  = (TextView) findViewById(R.id.info_client_legacy_police_number);
        info_client_hero  = (TextView) findViewById(R.id.info_client_hero);
        info_client_is_dependant  = (TextView) findViewById(R.id.info_client_is_dependant);
        info_client_primary_name  = (TextView) findViewById(R.id.info_client_primary_name);
        info_client_identification  = (TextView) findViewById(R.id.info_client_identification);
        linear_layout_primary_name  = (LinearLayout) findViewById(R.id.linear_layout_primary_name);
        linear_layout_hero  = (LinearLayout) findViewById(R.id.linear_layout_hero);
        linear_layout_police_number  = (LinearLayout) findViewById(R.id.linear_layout_police_number);
        linear_layout_identification  = (LinearLayout) findViewById(R.id.linear_layout_identification);
        linear_layout_dependant  = (LinearLayout) findViewById(R.id.linear_layout_dependant);
        linear_layout_company  = (LinearLayout) findViewById(R.id.linear_layout_company);
        linear_layout_client_name  = (LinearLayout) findViewById(R.id.linear_layout_client_name);
        info_box = (LinearLayout) findViewById(R.id.info_box);
        btn_send_mail = (Button) findViewById(R.id.btn_send_mail);
        user_message = (TextView) findViewById(R.id.user_message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        try {
            if (!info_client.equals("")) {
                obj = new JSONObject(info_client);
                if (obj.getJSONArray(CLIENTS).length() > 0) {


                    if (obj.getJSONArray(CLIENTS).length() == 1) {
                        printInfoClient(obj);
                    }
                }
            }else{
                text_global_name_number_layout.setVisibility(View.GONE);
                textView_client_status.setVisibility(View.GONE);
                linear_layout_company.setVisibility(View.GONE);
                linear_layout_dependant.setVisibility(View.GONE);
                linear_layout_identification.setVisibility(View.GONE);
                linear_layout_police_number.setVisibility(View.GONE);
                linear_layout_hero.setVisibility(View.GONE);
                linear_layout_primary_name.setVisibility(View.GONE);


                dob_not_update.setVisibility(View.VISIBLE);
                not_update_layout.setVisibility(View.VISIBLE);


                Date today = new Date();
                search_date_time.setText(("Date de recherche : " +
                        new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).format(today)));

                search_date_time.setPaintFlags(search_date_time.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


                textView_client_name.setText((firstname_str + " " + lastname_str));

                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    date = originalFormat.parse(dob_str);
                    String formattedDate = targetFormat.format(date);
                    textView_client_dob.setText(formattedDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(obj != null){
                        sendClientbyMail(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendClientbyMail(JSONObject obj) throws JSONException{


        progressDialog.setMessage("Patientez s'il vous plait ...");
        progressDialog.show();


        obj = (JSONObject) obj.getJSONArray(CLIENTS).get(0);



        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = originalFormat.parse(obj.getString("dob"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = targetFormat.format(date);

        is_dependant = false;

        if (obj.getInt(EMPLOYEE_ID)!= obj.getInt(PRIMARY_EMPLOYEE_ID))
            is_dependant = true;

        String name = obj.getString(FIRSTNAME);
        Log.d("INFO", name);
        Date today = new Date();
        String actual_date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(today);

        String firsname = obj.getString(FIRSTNAME);
        String lastname = obj.getString(LASTNAME) ;
        int global_name_number = obj.getInt(GLOBAL_NAME_NUMBER)  ;
        boolean status = obj.getBoolean("status") ;
        int employee_id = obj.getInt(EMPLOYEE_ID) ;
        String company = obj.getString(COMPANY) ;
//        String hero = obj.getString("hero_tag") ;
        String primary_name = obj.getString(PRIMARY_NAME) ;
        int primary_employee_id = obj.getInt(PRIMARY_EMPLOYEE_ID);
        String legacy_policy_number =  obj.getString(LEGACY_POLICE_NUMBER);
        int policy_number  = obj.getInt("policy_number");



        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);

        Call<SendResearch> call = retrofitInterfaces.sendResearch(obj, userInfo.getUserId(), Constants.TOKEN);

        call.enqueue(new Callback<SendResearch>() {
            @Override
            public void onResponse(Call<SendResearch> call, Response<SendResearch> response) {

                Log.d("INFO", response.toString());
                try{
                    SendResearch j = response.body();
                    Log.d("INFO", j.toString());
                    if(j.isMail()){
                        Toast.makeText(InfoClientActivity.this, "Mail envoyé", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(InfoClientActivity.this, SearchClientActivity.class);
                        startActivity(i);
                    }

                }catch (Exception e){
                    Toast.makeText(InfoClientActivity.this, "Erreur lors de la requête, essayer à nouveau.", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<SendResearch> call, Throwable t) {
                Log.d("INFO", t.getMessage());
                Toast.makeText(InfoClientActivity.this, "Problème de connexion", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }

   

    private void printInfoClient(JSONObject obj) throws JSONException {


//        Toast.makeText(this, obj.getLong(PRIMARY_EMPLOYEE_ID) + "", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,  obj.getLong(EMPLOYEE_ID) + "", Toast.LENGTH_SHORT).show();

        text_global_name_number_layout.setVisibility(View.VISIBLE);
        textView_client_global_name_number.setVisibility(View.VISIBLE);
        textView_client_status.setVisibility(View.VISIBLE);
        linear_layout_client_name.setVisibility(View.VISIBLE);
        textView_client_name.setVisibility(View.VISIBLE);
        textView_client_dob.setVisibility(View.VISIBLE);
        textView_client_global_name_number.setVisibility(View.VISIBLE);
        textView_client_status.setVisibility(View.VISIBLE);

        scrollView_info.setVisibility(View.VISIBLE);
        not_update_layout.setVisibility(View.GONE);
        //dob_not_update.setVisibility(View.GONE);

        obj = (JSONObject) obj.getJSONArray(CLIENTS).get(0);




        if(TextUtils.equals(obj.getString(LEGACY_POLICE_NUMBER), "12708")){
            info_box.setVisibility(View.GONE);
            textView_client_status.setVisibility(View.GONE);
            btn_send_mail.setVisibility(View.GONE);
            user_message.setVisibility(View.VISIBLE);
        }



        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = originalFormat.parse(obj.getString("dob"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);

        textView_client_dob.setText(formattedDate);

        if (obj.getLong(EMPLOYEE_ID)!= obj.getLong(PRIMARY_EMPLOYEE_ID))
            is_dependant = true;

        if(obj.has(FIRSTNAME) && obj.has(LASTNAME)){
            textView_client_name.setText((obj.getString(FIRSTNAME) + " " + obj.getString(LASTNAME)));
        }else{
            textView_client_name.setText((obj.getString("firstname") + " " + obj.getString("lastname")));
        }

        textView_client_global_name_number.setText(obj.getString(GLOBAL_NAME_NUMBER));

        Date today = new Date();
        search_date_time.setText(("Date de recherche : " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(today)));

        search_date_time.setPaintFlags(search_date_time.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        info_client_company.setText(obj.getString(COMPANY));
        info_client_legacy_police_number.setText(obj.getString(LEGACY_POLICE_NUMBER));
        info_client_identification.setText(obj.getString(EMPLOYEE_ID));
//        info_client_hero.setText(obj.getString("hero_tag"));

        if (is_dependant) {
            info_client_is_dependant.setText("OUI");
            info_client_primary_name.setText((obj.getString(PRIMARY_NAME) + " - " + obj.getLong(EMPLOYEE_ID)));
            linear_layout_primary_name.setVisibility(View.VISIBLE);
        }else {
            info_client_is_dependant.setText("NON");
            linear_layout_primary_name.setVisibility(View.GONE);
        }

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

        timer = new Timer();
        Log.i("Main", "Info Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask(InfoClientActivity.this);
        timer.schedule(logoutTimeTask, 600000); //auto logout in 10 minutes
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InfoClientActivity.this, SearchClientActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "Info cancel timer");
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            Log.i("Main", "Info cancel timer");
            timer = null;
        }
    }

}
