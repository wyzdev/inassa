package com.inassa.inassa.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inassa.inassa.R;
import com.inassa.inassa.activities.MainActivity;
import com.inassa.inassa.constants.Constants;
import com.inassa.inassa.interfaces.Communicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthBySwipe extends Fragment implements View.OnClickListener{


    public JSONObject jsonfromurl = null;
    public String address = "";

    Communicator communicator;

    TextView bonne_carte, mauvaise_carte, textView_error, textView_default;
    ProgressBar progressBar;
    LinearLayout linearLayout_lastname_container;
    Button button_validate;
    ImageView imageView_back;
    ImageButton imageButton_check, imageButton_close, imageButton_refresh;

    public AuthBySwipe() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        communicator = (Communicator) getActivity();

        bonne_carte = (TextView) view.findViewById(R.id.auth_by_swipe_textview_bonne_carte);
        mauvaise_carte = (TextView) view.findViewById(R.id.auth_by_swipe_textview_mauvaise_carte);
        textView_default = (TextView) view.findViewById(R.id.auth_by_swipe_textview_default_message);
        textView_error = (TextView) view.findViewById(R.id.auth_by_swipe_textview_error_message);
        progressBar = (ProgressBar) view.findViewById(R.id.auth_by_swipe_progressbar);
        linearLayout_lastname_container = (LinearLayout) view.findViewById(R.id.auth_by_swipe_linearlayout_container_last_name);
        imageView_back = (ImageView) view.findViewById(R.id.appbar_imageview_back);
        imageButton_close = (ImageButton) view.findViewById(R.id.auth_by_swipe_imagebutton_close);
        imageButton_refresh = (ImageButton) view.findViewById(R.id.auth_by_swipe_imagebutton_refresh);
        imageButton_check = (ImageButton) view.findViewById(R.id.auth_by_swipe_imagebutton_check);

        progressBar.setVisibility(View.GONE);
        linearLayout_lastname_container.setVisibility(View.GONE);
        textView_default.setVisibility(View.VISIBLE);
        textView_error.setVisibility(View.GONE);

        imageButton_check.setOnClickListener(this);
        imageButton_refresh.setOnClickListener(this);
        imageButton_close.setOnClickListener(this);
        imageView_back.setOnClickListener(this);


        /*
        * Pour le debuggage
        * a enlever apres debuggage
        * */
        bonne_carte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                goodCard();
            }
        });

        mauvaise_carte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                badCard();
            }
        });
        /*
        * Fin du debuggage
        * */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe, container, false);
    }

    private void badCard(){
        progressBar.setVisibility(View.GONE);
        textView_default.setVisibility(View.GONE);
        linearLayout_lastname_container.setVisibility(View.GONE);
        textView_error.setVisibility(View.VISIBLE);
    }

    private void goodCard(){
        progressBar.setVisibility(View.GONE);
        textView_error.setVisibility(View.GONE);
        textView_default.setVisibility(View.GONE);
        linearLayout_lastname_container.setVisibility(View.VISIBLE);

        imageButton_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicator.toInformationClient();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auth_by_swipe_imagebutton_check:
                //saveInLog("13sd5a1s2fd1a65", "Junior", "SAINT-FELIX", "2017-02-21", 1, 18.537682, -72.322805, "2eme ruelle nazon");
                getAddress();
                break;

            case R.id.auth_by_swipe_imagebutton_close:
                communicator.toAuthentification();
                break;

            case R.id.auth_by_swipe_imagebutton_refresh:
                communicator.resetReader();
               // communicator.setTimoutReader(10);
                communicator.toAuthBySwipe();
                break;

            case R.id.appbar_imageview_back:
                communicator.back();
                break;
        }
    }

    private void getAddress(){
        new JsonTask().execute("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Constants.LATITUDE + ',' + Constants.LONGITUDE);
    }

    public void saveInLog(final String global_number, final String first_name, final String last_name, final String date, final int status, final double latitude, final double longitude, final String postal_address) {
        //Toast.makeText(getActivity(), telephone + "\n" + telephone_user +"\n"+ montant +"\n"+ pin +"\n"+ Constant.APP_ID+"\n"+Constant.DEST_GROSSIST_DETAILLANT, Toast.LENGTH_LONG).show();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getActivity().getString(R.string.requesting));
        progressDialog.setMessage(getActivity().getString(R.string.wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, Constants.ADD_ADDRESS,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jso = new JSONObject(response);
                            Log.i("response", response);
                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            if (jso.getString("message").equals("Saved")) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Commit with success", Toast.LENGTH_SHORT).show();
                                communicator.toInformationClient();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Commit failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "return by server : " + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "L'erreur est : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_GLOBAL_NUMBER, global_number);
                params.put(Constants.KEY_FIRSTNAME, first_name);
                params.put(Constants.KEY_LASTNAME, last_name);
                params.put(Constants.KEY_DATE, date);
                params.put(Constants.KEY_STATUS, String.valueOf(status));
                params.put(Constants.KEY_LATITUDE, String.valueOf(latitude));
                params.put(Constants.KEY_LONGITUDE, String.valueOf(longitude));
                params.put(Constants.KEY_POSTAL_ADDRESS, postal_address);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            try {
                jsonfromurl = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (jsonfromurl.getString("status").equals("OK")){
                    JSONArray array = jsonfromurl.getJSONArray("results");
                    JSONObject obj_address = array.getJSONObject(0);
                    address = obj_address.getString("formatted_address");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    Log.i("current datetime", date);
                    Toast.makeText(getActivity(), "address : " + address + '\n' + "current datetime : " + date, Toast.LENGTH_SHORT).show();
                    saveInLog("13sd5a1s2fd1a65", "Junior", "SAINT-FELIX", date, 0, Constants.LATITUDE, Constants.LONGITUDE, address);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}



































