package com.inassa.inassa.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
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

import com.acs.audiojack.AesTrackData;
import com.acs.audiojack.AudioJackReader;
import com.acs.audiojack.DukptReceiver;
import com.acs.audiojack.DukptTrackData;
import com.acs.audiojack.Result;
import com.acs.audiojack.Track1Data;
import com.acs.audiojack.Track2Data;
import com.acs.audiojack.TrackData;
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
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthBySwipe extends Fragment implements View.OnClickListener{


    public static final String DEFAULT_MASTER_KEY_STRING = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
    public static final String DEFAULT_AES_KEY_STRING = "4E 61 74 68 61 6E 2E 4C 69 20 54 65 64 64 79 20";
    public static final String DEFAULT_IKSN_STRING = "FF FF 98 76 54 32 10 E0 00 00";
    public static final String DEFAULT_IPEK_STRING = "6A C2 92 FA A1 31 5B 4D 85 8A B3 A3 D7 D5 93 3A";

    public JSONObject jsonfromurl = null;
    public String address = "";
    private byte[] mAesKey = new byte[16];
    private byte[] mIksn = new byte[10];
    private byte[] mIpek = new byte[16];
    private DukptReceiver mDukptReceiver = new DukptReceiver();

    String track1Data_str = "";
    String track2Data_str = "";

    Communicator communicator;

    TextView bonne_carte, mauvaise_carte, textView_error, textView_default;
    ProgressBar progressBar;
    LinearLayout linearLayout_lastname_container;
    Button button_validate;
    ImageView imageView_back;
    ImageButton imageButton_check, imageButton_close, imageButton_refresh;
    private TextView test_text;

    private AudioManager mAudioManager;
    private AudioJackReader mReader;
    private ProgressDialog mProgress;


    public AuthBySwipe() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

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


        mReader.setOnTrackDataNotificationListener(new AudioJackReader.OnTrackDataNotificationListener() {

            private Timer mTimer;

            @Override
            public void onTrackDataNotification(AudioJackReader reader) {

            /* Show the progress. */
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        mProgress.setMessage("Processing the track data...");
                        mProgress.show();
                    }
                });
            /* Dismiss the progress after 5 seconds. */
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        mProgress.dismiss();
                        mTimer.cancel();
                    }
                }, 5000);
            }
        });

        mReader.setOnTrackDataAvailableListener(new AudioJackReader.OnTrackDataAvailableListener() {

            private Track1Data mTrack1Data;
            private Track2Data mTrack2Data;
            private Track1Data mTrack1MaskedData;
            private Track2Data mTrack2MaskedData;
            private String mTrack1MacString;
            private String mTrack2MacString;
            private String mBatteryStatusString;
            private String mKeySerialNumberString;
            private int mErrorId;

            @Override
            public void onTrackDataAvailable(AudioJackReader reader,
                                             TrackData trackData) {

                mTrack1Data = new Track1Data();
                mTrack2Data = new Track2Data();
                mTrack1MaskedData = new Track1Data();
                mTrack2MaskedData = new Track2Data();
                mTrack1MacString = "";
                mTrack2MacString = "";
                //mBatteryStatusString = toBatteryStatusString(trackData
                  //      .getBatteryStatus());
                mKeySerialNumberString = "";

            /* Hide the progress. */
               /* runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mProgress.dismiss();
                    };
                });*/

                if ((trackData.getTrack1ErrorCode() != TrackData.TRACK_ERROR_SUCCESS)
                        && (trackData.getTrack2ErrorCode() != TrackData.TRACK_ERROR_SUCCESS)) {
                    mErrorId = R.string.message_track_data_error_corrupted;
                } else if (trackData.getTrack1ErrorCode() != TrackData.TRACK_ERROR_SUCCESS) {
                    mErrorId = R.string.message_track1_data_error_corrupted;
                } else if (trackData.getTrack2ErrorCode() != TrackData.TRACK_ERROR_SUCCESS) {
                    mErrorId = R.string.message_track2_data_error_corrupted;
                }

            /* Show the track error. */
                if ((trackData.getTrack1ErrorCode() != TrackData.TRACK_ERROR_SUCCESS)
                        || (trackData.getTrack2ErrorCode() != TrackData.TRACK_ERROR_SUCCESS)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showMessageDialog(R.string.error, mErrorId);
                        }
                    });
                }

            /* Show the track data. */
                if (trackData instanceof AesTrackData) {
                    showAesTrackData((AesTrackData) trackData);
                } else if (trackData instanceof DukptTrackData) {
                    showDukptTrackData((DukptTrackData) trackData);
                }
            }

            /**
             * Shows the AES track data.
             *
             * @param trackData
             *            the AES track data.
             */
            private void showAesTrackData(AesTrackData trackData) {

                byte[] decryptedTrackData = null;

            /* Decrypt the track data. */
                try {

                    decryptedTrackData = aesDecrypt(mAesKey,
                            trackData.getTrackData());

                } catch (GeneralSecurityException e) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showMessageDialog(R.string.error,
                                    R.string.message_track_data_error_decrypted);
                        }
                    });

                /* Show the track data. */
                    showTrackData();
                    return;
                }

            /* Verify the track data. */
                if (!mReader.verifyData(decryptedTrackData)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showMessageDialog(R.string.error,
                                    R.string.message_track_data_error_checksum);
                        }
                    });
                /* Show the track data. */
                    showTrackData();
                    return;
                }

            /* Decode the track data. */
                mTrack1Data.fromByteArray(decryptedTrackData, 0,
                        trackData.getTrack1Length());
                mTrack2Data.fromByteArray(decryptedTrackData, 79,
                        trackData.getTrack2Length());

            /* Show the track data. */
                showTrackData();
            }

            /**
             * Shows the DUKPT track data.
             *
             * @param trackData
             *            the DUKPT track data.
             */
            private void showDukptTrackData(DukptTrackData trackData) {

                int ec = 0;
                int ec2 = 0;
                byte[] track1Data = null;
                byte[] track2Data = null;
                String track1DataString = null;
                String track2DataString = null;
                byte[] key = null;
                byte[] dek = null;
                byte[] macKey = null;
                byte[] dek3des = null;

                mKeySerialNumberString = toHexString(trackData.getKeySerialNumber());
                mTrack1MacString = toHexString(trackData.getTrack1Mac());
                mTrack2MacString = toHexString(trackData.getTrack2Mac());
                mTrack1MaskedData.fromString(trackData.getTrack1MaskedData());
                mTrack2MaskedData.fromString(trackData.getTrack2MaskedData());

            /* Compare the key serial number. */
                if (!DukptReceiver.compareKeySerialNumber(mIksn,
                        trackData.getKeySerialNumber())) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showMessageDialog(R.string.error,
                                    R.string.message_track_data_error_ksn);
                        }
                    });

                /* Show the track data. */
                    showTrackData();
                    return;
                }

            /* Get the encryption counter from KSN. */
                ec = DukptReceiver.getEncryptionCounter(trackData
                        .getKeySerialNumber());

            /* Get the encryption counter from DUKPT receiver. */
                ec2 = mDukptReceiver.getEncryptionCounter();

            /*
             * Load the initial key if the encryption counter from KSN is less
             * than the encryption counter from DUKPT receiver.
             */
                if (ec < ec2) {

                    mDukptReceiver.loadInitialKey(mIpek);
                    ec2 = mDukptReceiver.getEncryptionCounter();
                }

            /*
             * Synchronize the key if the encryption counter from KSN is greater
             * than the encryption counter from DUKPT receiver.
             */
                while (ec > ec2) {

                    mDukptReceiver.getKey();
                    ec2 = mDukptReceiver.getEncryptionCounter();
                }

                if (ec != ec2) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showMessageDialog(R.string.error,
                                    R.string.message_track_data_error_ec);
                        }
                    });

                /* Show the track data. */
                    showTrackData();
                    return;
                }

                key = mDukptReceiver.getKey();
                if (key == null) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                        /* Show the timeout. */
                            Toast.makeText(
                                    getActivity(),
                                    "The maximum encryption count had been reached.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                /* Show the track data. */
                    showTrackData();
                    return;
                }

                dek = DukptReceiver.generateDataEncryptionRequestKey(key);
                macKey = DukptReceiver.generateMacRequestKey(key);
                dek3des = new byte[24];

            /* Generate 3DES key (K1 = K3) */
                System.arraycopy(dek, 0, dek3des, 0, dek.length);
                System.arraycopy(dek, 0, dek3des, 16, 8);

                try {

                    if (trackData.getTrack1Data() != null) {

                    /* Decrypt the track 1 data. */
                        track1Data = tripleDesDecrypt(dek3des,
                                trackData.getTrack1Data());

                    /* Generate the MAC for track 1 data. */
                        mTrack1MacString += " ("
                                + toHexString(DukptReceiver.generateMac(macKey,
                                track1Data)) + ")";

                    /* Get the track 1 data as string. */
                        track1DataString = new String(track1Data, 1,
                                trackData.getTrack1Length(), "US-ASCII");

                    /* Divide the track 1 data into fields. */
                        mTrack1Data.fromString(track1DataString);
                    }

                    if (trackData.getTrack2Data() != null) {

                    /* Decrypt the track 2 data. */
                        track2Data = tripleDesDecrypt(dek3des,
                                trackData.getTrack2Data());

                    /* Generate the MAC for track 2 data. */
                        mTrack2MacString += " ("
                                + toHexString(DukptReceiver.generateMac(macKey,
                                track2Data)) + ")";

                    /* Get the track 2 data as string. */
                        track2DataString = new String(track2Data, 1,
                                trackData.getTrack2Length(), "US-ASCII");

                    /* Divide the track 2 data into fields. */
                        mTrack2Data.fromString(track2DataString);
                    }

                } catch (GeneralSecurityException e) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showMessageDialog(R.string.error,
                                    R.string.message_track_data_error_decrypted);
                        }
                    });

                } catch (UnsupportedEncodingException e) {
                }

            /* Show the track data. */
                showTrackData();
            }

            /**
             * Shows the track data.
             */
            private void showTrackData() {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    /* Increment the swipe count. */
                        //mSwipeCount++;

                        /*mTrackDataSwipeCountPreference.setSummary(Integer
                                .toString(mSwipeCount));
                        mTrackDataBatteryStatusPreference
                                .setSummary(mBatteryStatusString);
                        mTrackDataKeySerialNumberPreference
                                .setSummary(mKeySerialNumberString);*/
                        track1Data_str += (mTrack1MacString) + "\n\n";
                        track2Data_str += (mTrack2MacString) + "\n\n";

                                track1Data_str += "\n" + (mTrack1Data
                                .getJis2Data());
                        track1Data_str += "\n" + (concatString(
                                        mTrack1Data.getPrimaryAccountNumber(),
                                        mTrack1MaskedData.getPrimaryAccountNumber()));
                        track1Data_str += "\n" + (concatString(mTrack1Data.getName(),
                                        mTrack1MaskedData.getName()));
                        track1Data_str += "\n" + (concatString(
                                mTrack1Data.getExpirationDate(),
                                mTrack1MaskedData.getExpirationDate()));
                        track1Data_str += "\n" + (concatString(
                                mTrack1Data.getServiceCode(),
                                mTrack1MaskedData.getServiceCode()));
                        track1Data_str += "\n" + (concatString(
                                mTrack1Data.getDiscretionaryData(),
                                mTrack1MaskedData.getDiscretionaryData()));

                        track2Data_str += "\n" + (concatString(
                                        mTrack2Data.getPrimaryAccountNumber(),
                                        mTrack2MaskedData.getPrimaryAccountNumber()));
                        track2Data_str += "\n" + (concatString(
                                mTrack2Data.getExpirationDate(),
                                mTrack2MaskedData.getExpirationDate()));
                        track2Data_str += "\n" + (concatString(
                                mTrack2Data.getServiceCode(),
                                mTrack2MaskedData.getServiceCode()));
                        track2Data_str += "\n" + (concatString(
                                mTrack2Data.getDiscretionaryData(),
                                mTrack2MaskedData.getDiscretionaryData()));

                        Log.i("data_card", track1Data_str + "\n\n\n" + track2Data_str);

                        printData(track1Data_str + "\n\n\n" + track2Data_str);
                    }
                });
            }

            /**
             * Concatenates two strings with carriage return.
             *
             * @param string1
             *            the first string.
             * @param string2
             *            the second string.
             * @return the combined string.
             */
            private String concatString(String string1, String string2) {

                String ret = string1;

                if ((string1.length() > 0) && (string2.length() > 0)) {
                    ret += "\n";
                }

                ret += string2;

                return ret;
            }
            /* @Override
            public void onTrackDataAvailable(AudioJackReader audioJackReader, TrackData trackData) {
                if ((trackData.getTrack1ErrorCode() != TrackData.TRACK_ERROR_SUCCESS)
                        || (trackData.getTrack2ErrorCode() != TrackData.TRACK_ERROR_SUCCESS)) {

            *//* Show the track error. *//*
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (trackData instanceof AesTrackData) {

                    AesTrackData aesTrackData = (AesTrackData) trackData;
                    // Toast.makeText(MainActivity.this, "AES : " + aesTrackData, Toast.LENGTH_SHORT).show();
                    //aes = "AES : " + aesTrackData;
                    printData("AES : " + String.valueOf(aesTrackData));
                } else if (trackData instanceof DukptTrackData) {

                    DukptTrackData dukptTrackData = (DukptTrackData) trackData;
                    //Toast.makeText(MainActivity.this, "DUKPT : " + dukptTrackData, Toast.LENGTH_SHORT).show();
                    //dukpt = "DUKPT : " + dukptTrackData;
                    printData("DUKPT : " + String.valueOf(dukptTrackData));
                }

            }*/
        });
    }

    public void init(View view){
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
        test_text = (TextView) view.findViewById(R.id.test_card);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mReader = new AudioJackReader(mAudioManager);

        mProgress = new ProgressDialog(getActivity());
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        progressBar.setVisibility(View.GONE);
        linearLayout_lastname_container.setVisibility(View.GONE);
        textView_default.setVisibility(View.VISIBLE);
        textView_error.setVisibility(View.GONE);
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
                if (Constants.LATITUDE == -1 && Constants.LONGITUDE == -1){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    saveInLog("13sd5a1s2fd1a65", "Junior", "SAINT-FELIX", date, 1, Constants.LATITUDE, Constants.LONGITUDE, "");
                }
                else
                    getAddress();
                break;

            case R.id.auth_by_swipe_imagebutton_close:
                communicator.toAuthentification();
                break;

            case R.id.auth_by_swipe_imagebutton_refresh:
                resetReader();
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
                            //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(getActivity(), "address : " + address + '\n' + "current datetime : " + date, Toast.LENGTH_SHORT).show();
                    saveInLog("13sd5a1s2fd1a65", "John", "DOE", date, 0, Constants.LATITUDE, Constants.LONGITUDE, address);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void printData(String data){
        test_text.setText(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        mReader.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mReader.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mReader.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mReader.stop();
    }


    public void resetReader() {
        /* Reset the reader. */
        mReader.reset();
        Toast.makeText(getActivity(), "reader reset", Toast.LENGTH_SHORT).show();

    /* Set the reset complete callback. */
        /*mReader.setOnResetCompleteListener(
                new AudioJackReader.OnResetCompleteListener() {

                    @Override
                    public void onResetComplete(AudioJackReader reader) {

                    *//* TODO: Add your code here to process the notification. *//*
                        Toast.makeText(MainActivity.this, "Reset with success", Toast.LENGTH_SHORT).show();

                    }
                });*/
    }

    public void sleepReader() {

/* Enable the sleep mode. */
        mReader.sleep();
/* Set the result callback. */
        mReader.setOnResultAvailableListener(new AudioJackReader.OnResultAvailableListener() {
            @Override
            public void onResultAvailable(AudioJackReader audioJackReader, Result result) {
                Toast.makeText(getActivity(), "Sleep with success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTimoutReader(final int time) {


/* Set the sleep timeout to 10 seconds. */
        mReader.setSleepTimeout(time);

/* Set the result callback. */
       /* mReader.setOnResultAvailableListener(
                new AudioJackReader.OnResultAvailableListener() {

                    @Override
                    public void onResultAvailable(AudioJackReader reader, Result result) {
        *//* TODO: Add your code here to process the notification. *//*
                        Toast.makeText(MainActivity.this, "Timeout set to " + time + "sec", Toast.LENGTH_SHORT).show();


                    }
                });*/
    }

    public void getAudioJackStatus() {


/* Get the status. */
//        Toast.makeText(this, "The status of the audioJack" + mReader.getStatus(), Toast.LENGTH_SHORT).show();
/* Set the status callback. */
       /* mReader.setOnStatusAvailableListener(new AudioJackReader.OnStatusAvailableListener() {

            @Override
            public void onStatusAvailable(AudioJackReader reader, Status status) {
                Toast.makeText(MainActivity.this, "Status of audio jack" + status, Toast.LENGTH_SHORT).show();

        *//* TODO: Add your code here to process the status. *//*
            }
        });*/
    }
/*    private boolean checkResetVolume() {

        boolean ret = true;

        int currentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);

        int maxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if (currentVolume < maxVolume) {

            showMessageDialog(R.string.info, R.string.message_reset_info_volume);
            ret = false;
        }

        return ret;
    }*/

    /**
     * Shows the message dialog.
     *
     * @param titleId
     *            the title ID.
     * @param messageId
     *            the message ID.
     */
    private void showMessageDialog(int titleId, int messageId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(messageId)
                .setTitle(titleId)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });

        builder.show();
    }


    /**
     * Decrypts the data using AES.
     *
     * @param key
     *            the key.
     * @param input
     *            the input buffer.
     * @return the output buffer.
     * @throws GeneralSecurityException
     *             if there is an error in the decryption process.
     */
    private byte[] aesDecrypt(byte key[], byte[] input)
            throws GeneralSecurityException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(input);
    }


    /**
     * Converts the byte array to HEX string.
     *
     * @param buffer
     *            the buffer.
     * @return the HEX string.
     */
    private String toHexString(byte[] buffer) {

        String bufferString = "";

        if (buffer != null) {

            for (int i = 0; i < buffer.length; i++) {

                String hexChar = Integer.toHexString(buffer[i] & 0xFF);
                if (hexChar.length() == 1) {
                    hexChar = "0" + hexChar;
                }

                bufferString += hexChar.toUpperCase(Locale.US) + " ";
            }
        }

        return bufferString;
    }


    /**
     * Decrypts the data using Triple DES.
     *
     * @param key
     *            the key.
     * @param input
     *            the input buffer.
     * @return the output buffer.
     * @throws GeneralSecurityException
     *             if there is an error in the decryption process.
     */
    private byte[] tripleDesDecrypt(byte[] key, byte[] input)
            throws GeneralSecurityException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[8]);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(input);
    }
}



































