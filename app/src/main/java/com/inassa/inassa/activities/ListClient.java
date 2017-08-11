package com.inassa.inassa.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.inassa.inassa.R;
import com.inassa.inassa.adapers.RecyclerViewAdapter;
import com.inassa.inassa.tools.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListClient extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}
