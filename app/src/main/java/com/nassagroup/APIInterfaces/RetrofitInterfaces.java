package com.nassagroup.APIInterfaces;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nassagroup.core.Benefits;
import com.nassagroup.core.CheckLogin;
import com.nassagroup.core.Historic;
import com.nassagroup.core.LoginApi;
import com.nassagroup.core.LoginInassapp;
import com.nassagroup.core.SearchClient;
import com.nassagroup.core.SendResearch;
import com.nassagroup.core.Signout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by hollyn.derisse on 29/05/2018.
 */

public interface RetrofitInterfaces {

    @POST("RequestQuote/RequestLogin")
    Call<List<LoginApi>> postRawLoginApi(@Body JsonObject locationPost);

    @POST("RequestQuote/epic_mwClientSearch")
    Call<SearchClient> postRawSearchClient(@Body JsonObject locationPost);



    @POST("RequestQuote/epic_mwGetEBSubscriberExts")
    Call<Benefits> postRawGetBenefits(@Body JsonObject locationPost);

    @POST("users/requestUser.json")
    @FormUrlEncoded
    Call<LoginInassapp> loginInassap(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("token") String token);


    @POST("logs/add.json")
    @FormUrlEncoded
    Call<Historic> saveInHistoric(@Field("first_name") String first_name,
                                  @Field("last_name") String last_name,
                                  @Field("doctor_name") String doctor_name,
                                  @Field("status") int status,
                                  @Field("date") String date,
                                  @Field("dob") String dob,
                                  @Field("institution") String institution,
                                  @Field("token") String token,
                                  @Field("employee_id") int employee_id,
                                  @Field("is_dependant") int is_dependant,
                                  @Field("hero") String hero,
                                  @Field("primary_name") String primary_name,
                                  @Field("user_id") int user_id);

    @POST("users/checkcanlogin.json")
    @FormUrlEncoded
    Call<CheckLogin> canLogin(@Field("user_id") int user_id);


    @POST("clients/searchclientandroid.json")
    @FormUrlEncoded
    Call<SearchClient> searchClient(@Field("first_name") String firstname,
                                    @Field("last_name") String lastname,
                                    @Field("dob") String dob,
                                    @Field("user_id") int user_id,
                                    @Field("token") String token);

    @POST("users/deconnexion.json")
    @FormUrlEncoded
    Call<Signout> signout(@Field("user_id") int user_id);

    @POST("clients/sendresearchapi.json")
    @FormUrlEncoded
    Call<SendResearch> sendResearch(@Field("client")JSONObject client,
                                 @Field("user_id") int user_id,
                                 @Field("token") String token);



}
