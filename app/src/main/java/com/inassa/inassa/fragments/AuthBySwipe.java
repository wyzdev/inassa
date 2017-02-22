package com.inassa.inassa.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inassa.inassa.R;
import com.inassa.inassa.interfaces.Communicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthBySwipe extends Fragment {

    Communicator communicator;

    TextView bonne_carte, mauvaise_carte, textView_error, textView_default;
    ProgressBar progressBar;
    LinearLayout linearLayout_lastname_container;
    Button button_validate;

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
        button_validate = (Button) view.findViewById(R.id.auth_by_swipe_button_validate);

        progressBar.setVisibility(View.GONE);
        linearLayout_lastname_container.setVisibility(View.GONE);
        textView_default.setVisibility(View.VISIBLE);
        textView_error.setVisibility(View.GONE);

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
        textView_error.setVisibility(View.VISIBLE);
    }

    private void goodCard(){
        progressBar.setVisibility(View.GONE);
        textView_error.setVisibility(View.GONE);
        textView_default.setVisibility(View.GONE);
        linearLayout_lastname_container.setVisibility(View.VISIBLE);

        button_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicator.toInformationClient();
            }
        });
    }
}
