package com.example.jascaniojah.smartpagos;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jascaniojah.libraries.DataBaseHandler;
import com.example.jascaniojah.libraries.DateParser;
import com.example.jascaniojah.libraries.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Trans extends Fragment {

    TextView desde_fecha, hasta_fecha;
    EditText fecha_desde, fecha_hasta;
    ListView transacciones_list;
    //DatePickerDialog.OnDateSetListener date;
    static final int DATE_DIALOG_ID = 999;
    Calendar myCalendar;
    int val;
    Context context;
    private ArrayList<Movimientos> movimientosArray;

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
        Button getTranButton=(Button) view.findViewById(R.id.boton_consultar);

      //  new Movimientos().execute;

getTranButton.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {

        new getTransacciones().execute();


    }
});



       return view;
  }

    private class  getTransacciones extends AsyncTask<String,Void,JSONObject>
    {

        private ProgressDialog pDialog;
        String telefono, servicio,origen,imei, fechahora, fechainicio, fechafin,usuario;

        @Override
        protected void onPreExecute()             {

            super.onPreExecute();
            DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
            telefono="04249474436";
            servicio="04";
            origen="02";
            fechahora="2014-10-01 23:58:44";
            fechainicio="2014-01-01";
            fechafin="2015-01-01";
            HashMap cuenta = new HashMap();
            cuenta = db.getUser();
            usuario = cuenta.get("usuario").toString();
            imei= cuenta.get("imei").toString();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contacting Servers");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            UserFunctions jsonParser = new UserFunctions();
            JSONObject json= jsonParser.getTransacciones(telefono,servicio,origen,imei,fechahora,fechainicio,fechafin);
            Log.e("Response: ", "> " + json);

            movimientosArray = new ArrayList<Movimientos>();


            if (json != null) {
                try {

                    if (json != null) {
                        JSONArray movArray = json
                                .getJSONArray("transacciones");
                        Log.i("Trans.java", "JSONArray: " + movArray.toString());

                        // movimientosArray=Movimientos.fromJson(movArray);
                        for (int i = 0; i < movArray.length(); i++) {
                            String numero = movArray.getJSONObject(i).getString("numero");
                            Float monto = Float.parseFloat(movArray.getJSONObject(i).getString("monto"));
                            Date fecha= DateParser.StringToDateTime(movArray.getJSONObject(i).getString("fecha_hora"));
                            String producto=movArray.getJSONObject(i).getString("producto");
                            Movimientos mov = new Movimientos(numero, monto,fecha,producto);
                            movimientosArray.add(mov);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }



            return null;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            pDialog.dismiss();
            TransaccionesAdapter adapter = new TransaccionesAdapter(getActivity(), movimientosArray);
            ListView lv=(ListView) getActivity().findViewById(R.id.transacciones_list);
            lv.setAdapter(adapter);
            lv.setVisibility(View.VISIBLE);
        }





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
