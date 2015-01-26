package com.example.jascaniojah.smartpagos;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jascaniojah.libraries.DataBaseHandler;
import com.example.jascaniojah.libraries.DateParser;
import com.example.jascaniojah.libraries.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Trans extends Fragment {

    TextView desde_fecha, hasta_fecha;
    EditText fecha_desde, fecha_hasta;
    ListView transacciones_list;
    //DatePickerDialog.OnDateSetListener date;
    static final int DATE_DIALOG_ID = 999;
    Calendar myCalendar;
    Calendar c = Calendar.getInstance();
    int val;
    Context context;
    private ArrayList<Movimientos> movimientosArray;
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
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
        fecha_desde.setText(df1.format(c.getTime()));
        fecha_hasta.setText(df1.format(c.getTime()));
        Button getTranButton=(Button) view.findViewById(R.id.boton_consultar);

getTranButton.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {
        if(!fecha_hasta.getText().toString().equals("") && !fecha_desde.getText().toString().equals("")) {
            new getTransacciones().execute();
        }
        else
        {

            Toast.makeText(getActivity(),"Ingresar un Rango de fechas", Toast.LENGTH_SHORT).show();
        }

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
                try {
                fechafin=DateParser.StringToISO(fecha_hasta.getText().toString());
                fechainicio=DateParser.StringToISO(fecha_desde.getText().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando Transaciones");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            HashMap cuenta = new HashMap();
            DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
            cuenta = db.getUser();
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            //telefono=telephonyManager.getLine1Number().toString();
            //telefono = "04142222222";
            telefono= cuenta.get("telefono").toString();
            usuario = cuenta.get("usuario").toString();
            imei= cuenta.get("imei").toString();
            String  password=cuenta.get("password").toString();
            fechahora = df3.format(c.getTime());
            Log.e("Fecha",fechahora);

            UserFunctions jsonParser = new UserFunctions();
            JSONObject json= null;
            try {
                json = jsonParser.getEstadodeCuenta(telefono,imei,fechahora,fechainicio,fechafin,usuario,password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Response: ", "> " + json);

            movimientosArray = new ArrayList<Movimientos>();


            if (json != null) {
                try {

                    if (json != null) {
                        JSONArray movArray = json
                                .getJSONArray("Lista");
                        Log.i("Trans.java", "JSONArray: " + movArray.toString());

                        // movimientosArray=Movimientos.fromJson(movArray);
                        for (int i = 0; i < movArray.length(); i++) {
                            String numero = movArray.getJSONObject(i).getString("Serial");
                            Float monto = Float.parseFloat(movArray.getJSONObject(i).getString("Monto"));
                            Date fecha= DateParser.StringToDateTime(movArray.getJSONObject(i).getString("FechaHora"));
                            String producto=movArray.getJSONObject(i).getString("Tipo");
                            String id=movArray.getJSONObject(i).getString("idRespuesta");
                            Movimientos mov = new Movimientos(numero, monto,fecha,producto,id);
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
            if (movimientosArray.size()>0) {
                TransaccionesAdapter adapter = new TransaccionesAdapter(getActivity(), movimientosArray);
                ListView lv = (ListView) getActivity().findViewById(R.id.transacciones_list);
                lv.setAdapter(adapter);
                lv.setVisibility(View.VISIBLE);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("No se encontraron transacciones")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                Log.i("Trans.java","Array vacio:"+movimientosArray.toString());
            }
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
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("year", calender.get(Calendar.YEAR));

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
            String f=String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year);
            fecha_desde.setText(f);
            try {
                Log.i("Trans.java","Fecha desde parsed= "+DateParser.StringToString(f) );
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    OnDateSetListener ondate2 = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String f=String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year);
            fecha_hasta.setText(f);
            try {
                Log.i("Trans.java","Fecha hasta parsed= "+DateParser.StringToString(f) );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

}
