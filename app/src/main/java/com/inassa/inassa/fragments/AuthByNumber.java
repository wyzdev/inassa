package com.inassa.inassa.fragments;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.inassa.inassa.R;
import com.inassa.inassa.interfaces.Communicator;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthByNumber extends Fragment implements View.OnClickListener {

    EditText editText_birthdate, editText_client_number;
    ImageButton imageButton_calendar;
    Communicator communicator;
    ImageButton imageButton_check, imageButton_close, imageButton_refresh;
    ImageView imageView_back;

    public static final int MENU_ADD = Menu.FIRST;
    public static final int MENU_DELETE = Menu.FIRST + 1;


    private Calendar calendar;

    public AuthByNumber() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communicator = (Communicator) getActivity();
        editText_birthdate = (EditText) view.findViewById(R.id.auth_by_number_edittext_birthdate_client);
        editText_client_number = (EditText) view.findViewById(R.id.auth_by_number_edittext_number_client);
        imageView_back = (ImageView) view.findViewById(R.id.appbar_imageview_back);
        imageButton_close = (ImageButton) view.findViewById(R.id.auth_by_number_imagebutton_close);
        imageButton_refresh = (ImageButton) view.findViewById(R.id.auth_by_number_imagebutton_refresh);
        imageButton_check = (ImageButton) view.findViewById(R.id.auth_by_number_imagebutton_check);
        imageButton_calendar = (ImageButton) view.findViewById(R.id.auth_by_number_imagebutton_calendar);

        imageView_back.setVisibility(View.GONE);
        editText_birthdate.setEnabled(false);

        imageButton_check.setOnClickListener(this);
        imageButton_refresh.setOnClickListener(this);
        imageButton_close.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        imageButton_calendar.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_by_number, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auth_by_number_imagebutton_check:
                communicator.toInformationClient();
                break;

            case R.id.auth_by_number_imagebutton_close:
                communicator.toAuthentification();
                break;

            case R.id.auth_by_number_imagebutton_refresh:
                communicator.toAuthByNumber();
                break;

            case R.id.appbar_imageview_back:
                communicator.back();
                break;

            case R.id.auth_by_number_imagebutton_calendar:
                showDatePicker();
                break;
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String day = (String.valueOf(dayOfMonth).length() == 1) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
            String month = (String.valueOf(monthOfYear+1).length()== 1) ? "0" + String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);

            editText_birthdate.setText(day + " / " + month + " / " + String.valueOf(year));
        }
    };
}
