package com.inassa.inassa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inassa.inassa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationClientFragment extends Fragment {


    public InformationClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information_client, container, false);
    }

}
