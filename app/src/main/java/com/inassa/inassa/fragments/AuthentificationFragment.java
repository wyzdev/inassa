package com.inassa.inassa.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.inassa.inassa.R;
import com.inassa.inassa.interfaces.Communicator;


public class AuthentificationFragment extends Fragment{

    Button button_auth_by_swipe, button_auth_by_number;
    Communicator communicator;
    ImageView imageView_back;

    public AuthentificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communicator = (Communicator) getActivity();

        button_auth_by_swipe = (Button) view.findViewById(R.id.authentification_button_auth_by_swipe);
        button_auth_by_number = (Button) view.findViewById(R.id.authentification_button_auth_by_number);
        imageView_back = (ImageView) view.findViewById(R.id.appbar_imageview_back);

        imageView_back.setVisibility(View.GONE);

        button_auth_by_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicator.toAuthBySwipe();
            }
        });

        button_auth_by_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicator.toAuthByNumber();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentification, container, false);
    }
}
