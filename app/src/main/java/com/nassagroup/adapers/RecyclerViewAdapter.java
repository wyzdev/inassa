package com.nassagroup.adapers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.R;
import com.nassagroup.RetrofitClientInstance;
import com.nassagroup.activities.InfoClientActivity;
import com.nassagroup.core.Benefits;
import com.nassagroup.core.Historic;
import com.nassagroup.core.PolicyExtension;
import com.nassagroup.tools.Client;
import com.nassagroup.tools.Constants;
import com.nassagroup.tools.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hollyn.derisse on 10/08/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ClientViewHolder> {

    Context context;
    private final List<Client> clients;
    UserInfo userInfo;
    String hero_name, key;
    DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date;

    public RecyclerViewAdapter(Context context, List<Client> clients) {
        this.clients = clients;
        this.context = context;
        userInfo = new UserInfo(context);
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_row, viewGroup, false);
        ClientViewHolder cvh = new ClientViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final ClientViewHolder clientViewHolder, final int i) {
        date = new Date();
        try {
            date = originalFormat.parse(clients.get(i).getDob());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        clientViewHolder.client_firstname.setText(clients.get(i).getFirstname());
        clientViewHolder.client_dob.setText(targetFormat.format(date));
        clientViewHolder.client_lastname.setText(clients.get(i).getLastname());
        clientViewHolder.client_address.setText(clients.get(i).getAddress());
        clientViewHolder.client_legacy_policy_number.setText(String.valueOf(clients.get(i).getLegacy_policy_number()));
        clientViewHolder.client_company.setText(clients.get(i).getCompany());
        clientViewHolder.client_identification.setText(String.valueOf(clients.get(i).getEmployeeId()));
        clientViewHolder.client_hero.setText(clients.get(i).getHero_tag());

        if (clients.get(i).isDependant()) {
            clientViewHolder.client_is_dependant.setText(context.getResources().getString(R.string.yes));
            clientViewHolder.client_primary_name.setText((clients.get(i).getEmployeeId() + " - " + clients.get(i).getPrimaryName()));
        }else {
            clientViewHolder.client_is_dependant.setText(context.getResources().getString(R.string.no));
            clientViewHolder.client_primary_name.setText("-");
        }

        if (clients.get(i).isStatus()){
            clientViewHolder.client_status.setText(context.getResources().getString(R.string.active));
            clientViewHolder.client_status.setBackgroundColor(context.getResources().getColor(R.color.color_background_active));
            clientViewHolder.client_status.setTextColor(context.getResources().getColor(R.color.white));
        } else{
            clientViewHolder.client_status.setText(context.getResources().getString(R.string.inactive));
        }


        // check if this client is active
        if (!clients.get(i).isStatus()) {
            // change the color of the background
            clientViewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.color_background_inactive));

            // change the colore of the foreground
            clientViewHolder.client_hero.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_is_dependant.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_primary_name.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_status.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_identification.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_firstname.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_dob.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_lastname.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_address.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_legacy_policy_number.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
            clientViewHolder.client_company.setTextColor(context.getResources().getColor(R.color.color_foreground_inactive));
        } else {
            clientViewHolder.client_hero.setTextColor(context.getResources().getColor(R.color.white));
            clientViewHolder.client_hero.setBackgroundColor(context.getResources().getColor(R.color.color_background_hero));

        }

        clientViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info_client = "{\n" +
                        "     \"error\": false,\n" +
                        "     \"clients\": [\n" +
                        "       {\n" +
                        "         \"global_name_number\": " + clients.get(i).getGlobal_name_number() + ",\n" +
                        "         \"firstname\": \"" + clients.get(i).getFirstname() + "\",\n" +
                        "         \"lastname\": \"" + clients.get(i).getLastname() + "\",\n" +
                        "         \"dob\": \"" + clients.get(i).getDob() + "\",\n" +
                        "         \"address\": \"" + clients.get(i).getAddress() + "\",\n" +
                        "         \"policy_number\": " + clients.get(i).getPolicy_number() + ",\n" +
                        "         \"company\": \"" + clients.get(i).getCompany() + "\",\n" +
                        "         \"status\": " + clients.get(i).isStatus() + ",\n" +
                        "         \"employee_id\": "+ clients.get(i).getEmployeeId() +",\n" +
                        "         \"is_dependant\": "+ clients.get(i).isDependant() +",\n" +
//                        "         \"hero\": "+ clients.get(i).getHero() +"\n" +
                        "         \"primary_name\": \""+ clients.get(i).getPrimaryName() +"\",\n" +
                        "         \"legacy_policy_number\": \""+ clients.get(i).getLegacy_policy_number() +"\",\n" +
                        "         \"hero_tag\": \""+ clients.get(i).getHero_tag() +"\",\n" +
                        "         \"primary_employee_id\": "+ clients.get(i).getPrimaryEmployeeId() +"\n" +
                        "       }\n" +
                        "     ]\n" +
                        "   }";

                saveInLogs(info_client);

            }
        });
    }

    public void proceedGetBenefits(final ProgressDialog progressDialog, final int employee_id,
                                   final String firstname, final String info_client) {

        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClient().create(RetrofitInterfaces.class);

//        Toast.makeText(context, userInfo.getKey() +
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
                Benefits benefits = response.body();
                if (!benefits.isSuccess()){
                    Toast.makeText(context, benefits.getError_message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<PolicyExtension> policyExtensions = benefits.getPolicyExtensions();

                if (policyExtensions != null) {
                    int pos_extension_hero = posExtensionHero(policyExtensions);
                    if (pos_extension_hero != -1) {
                        hero_name = policyExtensions.get(pos_extension_hero).getNameForDisplay();
                    } else {
                        hero_name = "N/A";
                    }
//                    Toast.makeText(context, hero_name, Toast.LENGTH_SHORT).show();

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

    public void proceedSaveInHistoric(final String info_client,
                                      String first_name,
                                      String last_name,
                                      String doctor_name,
                                      int status,
                                      String date,
                                      String dob,
                                      String institution,
                                      String token,
                                      int employee_id,
                                      int is_dependant,
                                      String hero,
                                      String primary_name,
                                      int user_id) {
        if (is_dependant == 0)
            primary_name = "-";

        /*Create handle for the RetrofitInstance interface*/
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<Historic> call = retrofitInterfaces
                .saveInHistoric(first_name,
                        last_name,
                        doctor_name,
                        status,
                        date,
                        dob,
                        institution,
                        token,
                        employee_id,
                        is_dependant,
                        hero,
                        employee_id + " - " + primary_name,
                        user_id);
        call.enqueue(new Callback<Historic>() {
            @Override
            public void onResponse(Call<Historic> call, retrofit2.Response<Historic> response) {
                //progressDialog.dismiss();
                Historic historic = response.body();
                if (historic != null) {
                    if (historic.error) {
                        Toast.makeText(context, "Erreur d'enregistrement" , Toast.LENGTH_LONG).show();

                        return;
                    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    Intent intent = new Intent(context, InfoClientActivity.class);
                    Bundle extras = new Bundle();

                    extras.putString("info_client", info_client);
                    extras.putString("hero_name", hero_name);
                    intent.putExtras(extras);


                    context.startActivity(intent);

                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } else {
                    Toast.makeText(context, "Client inconnu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historic> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong...Please try later!" ,
                        Toast.LENGTH_LONG).show();

            }

        });
    }

    private int posExtensionHero(List<PolicyExtension> list_extensions) {
        for (int i = 0; i < list_extensions.size(); i++) {
            String extension = list_extensions.get(i).getExtension().toLowerCase();
            if (extension.contains("hero")) {
                return i;
            }
        }
        return -1;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Method that saves the client's information retrieved and doctor's full name
     * and institution in online database.
     *
     * @param info_client
     */
    private void saveInLogs(final String info_client) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Enregistrement ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject obj;
        String firstname = "";
        int employee_id = 0;

        String key = userInfo.getKey();
        try {
            obj = new JSONObject(info_client);
            if (!obj.getBoolean("error") && obj.getJSONArray("clients").length() > 0) {

                obj = (JSONObject) obj.getJSONArray("clients").get(0);
                employee_id = obj.getInt("employee_id");

                firstname = obj.getString("firstname");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        proceedGetBenefits(progressDialog, employee_id, firstname, info_client);
        Intent intent = new Intent(context, InfoClientActivity.class);
        Bundle extras = new Bundle();

        extras.putString("info_client", info_client);
//        extras.putString("hero_name", hero_name);
        intent.putExtras(extras);


        context.startActivity(intent);


    }
/*
    private void saveInLogs(final String info_client) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Enregistrement ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.show();

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date();

        JSONObject obj;
        String firstname = "";
        String lastname = "";
        boolean status = false;
        String dob = "";
        int employee_id = 0;
        boolean is_dependant = false;
        String hero = "N/A";
        String primary_name = "";
        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            obj = new JSONObject(info_client);
            if (obj.getBoolean("success") && obj.getJSONArray("clients").length() > 0) {

                obj = (JSONObject) obj.getJSONArray("clients").get(0);
                employee_id = obj.getInt("employee_id");
                is_dependant = obj.getBoolean("is_dependant");
                hero = obj.getString("hero");
                primary_name = obj.getString("primary_name");

                firstname = obj.getString("first_name");
                lastname = obj.getString("last_name");
                status = obj.getBoolean("status");
                Date birthdate = originalFormat.parse(obj.getString("dob"));
                dob = targetFormat.format(birthdate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        final String finalFirstname = firstname;
        final String finalLastname = lastname;
        final boolean finalStatus = status;
        final String finalDob = dob;
        final int finalEmployee_id = employee_id;
        final boolean finalIs_dependant = is_dependant;
        final String finalHero = hero;
        final String finalPrimary_name = primary_name;

        Toast.makeText(context, finalFirstname + " " + finalLastname + " " + finalStatus + " " + finalDob + " " + finalEmployee_id + " " + finalIs_dependant + " " + finalHero + " " + finalPrimary_name, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
                        try {
                            JSONObject jso = new JSONObject(response);

                            if (!jso.getBoolean("error")) {
                                Log.i("info_2", info_client);
                                Intent intent = new Intent(context, InfoClientActivity.class);
                                intent.putExtra("info_client", info_client);
                                context.startActivity(intent);

                            } else {
                                Toast.makeText(context, "Client inconnu.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(context, "Erreur! Essayez encore.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_FIRSTNAME, finalFirstname.toUpperCase());
                params.put(Constants.KEY_LASTNAME, finalLastname.toUpperCase());
                params.put(Constants.KEY_DOCTOR, userInfo.getUserFirstname() + " " + userInfo.getUserLastname());
                params.put(Constants.KEY_STATUS, String.valueOf((finalStatus) ? 1 : 0));
                params.put(Constants.KEY_DATE, dateFormat.format(date));
                params.put(Constants.KEY_DOB, finalDob);
                params.put(Constants.KEY_INSTITUTION, userInfo.getUserInstitution());
                params.put(Constants.KEY_TOKEN, Constants.TOKEN);


                params.put(Constants.KEY_CLIENT_ID, String.valueOf(finalEmployee_id));
                params.put(Constants.KEY_IS_DEPENDANT, String.valueOf(finalIs_dependant));
                params.put(Constants.KEY_HERO, finalHero);
                params.put(Constants.KEY_PRIMARY_NAME, finalPrimary_name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }*/

    @Override
    public int getItemCount() {
        return clients.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView client_firstname;
        TextView client_lastname;
        TextView client_dob;
        TextView client_address;
        TextView client_legacy_policy_number;
        TextView client_company;
        TextView client_identification;
        TextView client_primary_name;
        TextView client_is_dependant;
        TextView client_status;
        TextView client_hero;

        ClientViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            client_address = (TextView) itemView.findViewById(R.id.client_address);
            client_company = (TextView) itemView.findViewById(R.id.client_company);
            client_dob = (TextView) itemView.findViewById(R.id.client_dob);
            client_firstname = (TextView) itemView.findViewById(R.id.client_firstname);
            client_lastname = (TextView) itemView.findViewById(R.id.client_lastname);
            client_legacy_policy_number = (TextView) itemView.findViewById(R.id.client_legacy_policy_number);
            client_identification = (TextView) itemView.findViewById(R.id.client_identification);
            client_primary_name = (TextView) itemView.findViewById(R.id.client_primary_name);
            client_is_dependant = (TextView) itemView.findViewById(R.id.client_is_dependant);
            client_status = (TextView) itemView.findViewById(R.id.client_status);
            client_hero = (TextView) itemView.findViewById(R.id.client_hero);
        }
    }
}
