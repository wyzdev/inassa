package com.inassa.inassa.adapers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inassa.inassa.R;
import com.inassa.inassa.activities.InfoClientActivity;
import com.inassa.inassa.tools.Client;
import com.inassa.inassa.tools.UserInfo;

import java.util.List;

/**
 * Created by hollyn.derisse on 10/08/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ClientViewHolder> {

    Context context;
    private final List<Client> clients;

    public RecyclerViewAdapter(Context context, List<Client> clients) {
        this.clients = clients;
        this.context = context;
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
                Intent intent = new Intent(context, InfoClientActivity.class);
                intent.putExtra("info_client", "{\n" +
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
                        "   }");
                context.startActivity(intent);

            }
        });
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
