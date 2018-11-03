package com.nassagroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nassagroup.tools.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hollyn.derisse on 29/05/2018.
 */

public class RetrofitClientInstance {

//    private static String baseUrl = "http://192.168.5.8:8080/";
    private static String baseUrl = Constants.SERVER_INASSA;
//    private static String baseUrl_server = "http://192.168.5.8:8765/inassa_web/";
    private static String baseUrl_server = Constants.SERVER;

    private static Retrofit retrofit=null;
    public static Retrofit getClient(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG){
            okHttpClientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }

    public static Retrofit getClientForInassapp(){

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder().setLenient().create();

        if(BuildConfig.DEBUG){
            okHttpClientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl_server)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        return retrofit;
    }
}