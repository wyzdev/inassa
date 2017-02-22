package com.inassa.inassa.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.inassa.inassa.R;
import com.inassa.inassa.interfaces.Communicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthByNumber extends Fragment  {

    EditText editText_birthdate, editText_client_number;
    Button button_validate;
    ImageButton imageButton_calendar;
    Communicator communicator;

    public AuthByNumber() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communicator = (Communicator) getActivity();
        editText_birthdate = (EditText) view.findViewById(R.id.auth_by_number_edittext_birthdate_client);
        editText_client_number = (EditText) view.findViewById(R.id.auth_by_number_edittext_number_client);
        button_validate = (Button) view.findViewById(R.id.auth_by_number_button_validate);

        imageButton_calendar = (ImageButton) view.findViewById(R.id.auth_by_number_imagebutton_calendar);

        imageButton_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();
            }
        });

        button_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicator.toInformationClient();
            }
        });
    }

    public void getDate(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_by_number, container, false);
    }
}
