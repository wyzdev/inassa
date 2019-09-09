package com.nassagroup;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.core.KeyRequestUser;
import com.nassagroup.core.Request;
import com.nassagroup.core.ResponseKey;
import com.nassagroup.tools.ObjectBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */
public class App extends Application {

    KeyRequestUser keyRequestUser;
    Integer tries;
    static Context context;
    static App mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mInstance = this;
        ObjectBox.init(this);
        keyRequestUser = new KeyRequestUser();
        keyRequestUser.setPassword();
        keyRequestUser.setUsername();
        tries = 0;
//        requestKeyInBackground();
    }

    public static Context getContext() {
        return context;
    }

    public static App getInstance() {
        return mInstance;
    }

    public KeyRequestUser getKeyRequestUser() {
        return keyRequestUser;
    }

    public void setKeyRequestUser(KeyRequestUser keyRequestUser) {
        this.keyRequestUser = keyRequestUser;
    }


    public void requestKeyInBackground() {
        final Request request = new Request();
        request.setKeyRequestUser(keyRequestUser);
        Log.e("HTTP_REQUEST", request.toString());
        Thread thread = new Thread() {
            @Override
            public void run() {
                RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getDirectAPIConnexion().create(RetrofitInterfaces.class);
                Call<List<ResponseKey>> call = retrofitInterfaces.requestKeys(request);
                call.enqueue(new Callback<List<ResponseKey>>() {
                    @Override
                    public void onResponse(Call<List<ResponseKey>> call, retrofit2.Response<List<ResponseKey>> response) {
                        try {
                            Log.e("HTTP_RESPONSE2", response.body().toString());
                            Log.e("HTTP_RESPONSE3", response.body().get(0).getResponses().get(0).getKey());
                            keyRequestUser.setKey(response.body().get(0).getResponses().get(0).getKey());
                            keyRequestUser.setDate(new Date());
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(), "Key found : " + keyRequestUser.getKey(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("HTTP_ERROR_MESSAGE", e.getMessage());
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(), "Key not found : ", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ResponseKey>> call, Throwable t) {
                        Log.e("HTTP_ERROR_MESSAGE", t.getMessage());
                        tries++;
                        if (tries <= 5) {
                            requestKeyInBackground();
                        }
                    }
                });
            }
        };
        thread.start();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
