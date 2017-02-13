package com.inassa.inassa.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.inassa.inassa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthByNumber extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    EditText editText_birthdate, editText_client_number;
    Button button_validate;

    public AuthByNumber() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText_birthdate = (EditText) view.findViewById(R.id.auth_by_number_edittext_birthdate_client);
        editText_client_number = (EditText) view.findViewById(R.id.auth_by_number_edittext_number_client);
        button_validate = (Button) view.findViewById(R.id.auth_by_number_button_validate);

        getDate();
    }

    public void getDate(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_by_number, container, false);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        editText_client_number.setText(date);
    }
}
