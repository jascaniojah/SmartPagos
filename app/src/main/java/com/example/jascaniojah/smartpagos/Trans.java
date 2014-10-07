package com.example.jascaniojah.smartpagos;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


public class Trans extends Fragment {

    TextView desde_fecha, hasta_fecha;
    EditText fecha_desde, fecha_hasta;
    //DatePickerDialog.OnDateSetListener date;
    static final int DATE_DIALOG_ID = 999;
    Calendar myCalendar;
    int val;
    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trans_frag, container, false);

        desde_fecha = (TextView) view.findViewById(R.id.desde_fecha);
        hasta_fecha = (TextView) view.findViewById(R.id.hasta_fecha);
        fecha_desde = (EditText) view.findViewById(R.id.fecha_desde);
        fecha_hasta = (EditText) view.findViewById(R.id.fecha_hasta);
        fecha_desde.setFocusableInTouchMode(false);
        fecha_hasta.setFocusableInTouchMode(false);
        myCalendar = Calendar.getInstance();

        return view;

    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        fecha_desde.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                val = 0;
                showDatePicker(val);

            }
        });

        fecha_hasta.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                val = 1;
                showDatePicker(val);
            }
        });
    }


    private void showDatePicker(int i) {
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
        if (i == 0) {
            date.setCallBack(ondate);
            date.show(getFragmentManager(), "Date Picker");
        }

        if(i==1){
            date.setCallBack(ondate2);
            date.show(getFragmentManager(), "Date Picker");
        }

    }

    OnDateSetListener ondate = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            fecha_desde.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));

        }
    };

    OnDateSetListener ondate2 = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            fecha_hasta.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));

        }
    };

}

