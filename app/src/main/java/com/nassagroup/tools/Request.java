package com.nassagroup.tools;

import android.app.ProgressDialog;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.RetrofitClientInstance;
import com.nassagroup.core.Benefits;
import com.nassagroup.core.LoginApi;
import com.nassagroup.core.PolicyExtension;
import com.nassagroup.core.SearchClient;
import com.nassagroup.core.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hollyn.derisse on 29/05/2018.
 */

public class Request {

    private static String key;

    public static String proceedLogin(final ProgressDialog progressDialog){
        /*Create handle for the RetrofitInstance interface*/
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClient().create(RetrofitInterfaces.class);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse("{\"Login\":{\"username\":\"jotest@test.com\",\"password\":\"P@$$w0rd\"}}").getAsJsonObject();
        Call<List<LoginApi>> call = retrofitInterfaces.postRawLoginApi(o);
        call.enqueue(new Callback<List<LoginApi>>() {
            @Override
            public void onResponse(Call<List<LoginApi>> call, Response<List<LoginApi>> response) {
                progressDialog.dismiss();
                List<LoginApi> l = response.body();
                LoginApi loginApi = l.get(0);
                key = loginApi.getResponse().get(0).getKey();
            }

            @Override
            public void onFailure(Call<List<LoginApi>> call, Throwable t) {
                progressDialog.dismiss();

            }

        });
        return key;
    }

    public static List<Client> proceedSearchClient(final ProgressDialog progressDialog, final String firstname, final String lastname, final String dob){
        final List<Client>[] clients = new List[0];
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClient().create(RetrofitInterfaces.class);

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse("{\"resquestkey\":{\"key\":\"" + key + "\"},\"first_name\":\"" + firstname + "\",\"last_name\":\"" + lastname + "\",\"dob\":\"" + dob + "\"}").getAsJsonObject();

        Call<SearchClient> call = retrofitInterfaces.postRawSearchClient(o);
        call.enqueue(new Callback<SearchClient>() {
            @Override
            public void onResponse(Call<SearchClient> call, Response<SearchClient> response) {
                progressDialog.dismiss();
                SearchClient searchClient = response.body();
                clients[0] = searchClient.getClients();
            }

            @Override
            public void onFailure(Call<SearchClient> call, Throwable t) {
                progressDialog.dismiss();
            }

        });
        return clients[0];
    }


    public static String proceedGetBenefits(final ProgressDialog progressDialog, final int employee_id, final String firstname){

        final String[] hero_name = {"N/A"};
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClient().create(RetrofitInterfaces.class);

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse("{\"resquestkey\":{\"key\":\"" + key + " \"},\"employee_id\": " + employee_id +",\"first_name\": \""+ firstname +"\"}").getAsJsonObject();

        Call<Benefits> call = retrofitInterfaces.postRawGetBenefits(o);
        call.enqueue(new Callback<Benefits>() {
            @Override
            public void onResponse(Call<Benefits> call, Response<Benefits> response) {
                progressDialog.dismiss();
                Benefits Benefits = response.body();
                List<PolicyExtension> policyExtensions = Benefits.getPolicyExtensions();

                int pos_extension_hero = posExtensionHero(policyExtensions);
                if (pos_extension_hero != -1){
                    hero_name[0] = policyExtensions.get(pos_extension_hero).getNameForDisplay();
                }
            }

            @Override
            public void onFailure(Call<Benefits> call, Throwable t) {
                progressDialog.dismiss();

            }

        });
        return hero_name[0];
    }

    private static int posExtensionHero(List<PolicyExtension> list_extensions){
        for(int i = 0; i < list_extensions.size(); i++){
            String extension = list_extensions.get(i).getExtension().toLowerCase();
            if (extension.contains("hero")){
                return i;
            }
        }
        return -1;
    }

}
