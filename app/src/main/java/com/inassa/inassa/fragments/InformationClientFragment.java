package com.inassa.inassa.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.inassa.inassa.R;
import com.inassa.inassa.interfaces.Communicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationClientFragment extends Fragment implements View.OnClickListener{

    ImageView imageView_back;
    ImageButton imageButton_close;
    Communicator communicator;

    public InformationClientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        communicator = (Communicator) getActivity();

        imageView_back = (ImageView) view.findViewById(R.id.appbar_imageview_back);
        imageButton_close = (ImageButton) view.findViewById(R.id.information_client_imagebutton_close);

        imageButton_close.setOnClickListener(this);
        imageView_back.setOnClickListener(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information_client, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.information_client_imagebutton_close:
                communicator.toAuthentification();
                break;

            case R.id.appbar_imageview_back:
                communicator.back();
                break;
        }
    }
}
