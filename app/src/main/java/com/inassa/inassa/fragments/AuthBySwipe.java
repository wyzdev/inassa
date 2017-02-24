package com.inassa.inassa.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inassa.inassa.R;
import com.inassa.inassa.interfaces.Communicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthBySwipe extends Fragment implements View.OnClickListener{

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
                communicator.toInformationClient();
                break;

            case R.id.auth_by_swipe_imagebutton_close:
                communicator.toAuthentification();
                break;

            case R.id.auth_by_swipe_imagebutton_refresh:
                communicator.toAuthBySwipe();
                break;

            case R.id.appbar_imageview_back:
                communicator.back();
                break;
        }
    }
}
