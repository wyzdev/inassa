package com.nassagroup.adapers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.nassagroup.R;
import com.nassagroup.activities.InfoClientActivity;
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

/**
 * Created by hollyn.derisse on 10/08/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ClientViewHolder> {

    Context context;
    private final List<Client> clients;
    UserInfo userInfo;

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
        clientViewHolder.client_firstname.setText(clients.get(i).getFirstname());
        clientViewHolder.client_dob.setText(clients.get(i).getDob());
        clientViewHolder.client_lastname.setText(clients.get(i).getLastname());
        clientViewHolder.client_address.setText(clients.get(i).getAddress());
        clientViewHolder.client_policy_number.setText(clients.get(i).getPolicy_number());
        clientViewHolder.client_company.setText(clients.get(i).getCompany());

        clientViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info_client = "{\n" +
                        "     \"success\": true,\n" +
                        "     \"clients\": [\n" +
                        "       {\n" +
                        "         \"global_name_number\": "+ clients.get(i).getGlobal_name_number() +",\n" +
                        "         \"first_name\": \""+ clients.get(i).getFirstname() +"\",\n" +
                        "         \"last_name\": \""+ clients.get(i).getLastname() +"\",\n" +
                        "         \"dob\": \""+ clients.get(i).getDob() +"\",\n" +
                        "         \"address\": \"" +clients.get(i).getAddress()+ "\",\n" +
                        "         \"policy_number\": "+ clients.get(i).getPolicy_number() +",\n" +
                        "         \"company\": \""+ clients.get(i).getCompany() +"\",\n" +
                        "         \"status\": "+ clients.get(i).isStatus() +"\n" +
                        "       }\n" +
                        "     ]\n" +
                        "   }";

                saveInLogs(info_client);

            }
        });
    }
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
        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            obj = new JSONObject(info_client);
            if (obj.getBoolean("success") && obj.getJSONArray("clients").length() > 0){

                obj = (JSONObject) obj.getJSONArray("clients").get(0);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
                        try {
                            JSONObject jso  = new JSONObject(response);

                            if (!jso.getBoolean("error")){
                                Log.i("info_2", info_client);
                                Intent intent = new Intent(context, InfoClientActivity.class);
                                intent.putExtra("info_client", info_client);
                                context.startActivity(intent);

                            }
                            else{
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
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

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
        TextView client_policy_number;
        TextView client_company;

        ClientViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            client_address = (TextView) itemView.findViewById(R.id.client_address);
            client_company = (TextView) itemView.findViewById(R.id.client_company);
            client_dob = (TextView) itemView.findViewById(R.id.client_dob);
            client_firstname = (TextView) itemView.findViewById(R.id.client_firstname);
            client_lastname = (TextView) itemView.findViewById(R.id.client_lastname);
            client_policy_number = (TextView) itemView.findViewById(R.id.client_policy_number);
        }
    }
}
