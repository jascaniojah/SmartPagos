package com.example.jascaniojah.smartpagos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jascaniojah.libraries.DataBaseHandler;
import com.example.jascaniojah.libraries.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class Notificar extends Fragment {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
    private static String KEY_ERROR = "error";
    TextView cuenta, referencia, monto,origen,registerErrorMsg;
    EditText num_cuenta, num_referencia, monto_deposito,cuenta_origen;
    Button boton_notificar;

    Spinner spnr;
    String[] caso = {
            "Deposito",
            "Transferencia Electronica",

    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificar_frag, container, false);
        cuenta = (TextView) view.findViewById(R.id.cuenta);
        referencia = (TextView) view.findViewById(R.id.referencia);
        monto = (TextView) view.findViewById(R.id.monto);
        num_cuenta = (EditText) view.findViewById(R.id.numero_cuenta);
        num_referencia = (EditText) view.findViewById(R.id.num_referencia);
        monto_deposito = (EditText) view.findViewById(R.id.monto_deposito);
        origen = (TextView) view.findViewById(R.id.origen);
        cuenta_origen = (EditText) view.findViewById(R.id.cuenta_origen);
        boton_notificar = (Button) view.findViewById(R.id.boton_notificar);
        registerErrorMsg = (TextView) view.findViewById(R.id.notificar_error);

        spnr = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, caso);
        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = spnr.getSelectedItemPosition();

                        if(position==0){
                            origen.setEnabled(false);
                            cuenta_origen.setEnabled(false);

                        }

                        if(position==1){
                            origen.setEnabled(true);
                            cuenta_origen.setEnabled(true);

                        }

                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );




        boton_notificar.setOnClickListener(new View.OnClickListener() {
            @Override
                            public void onClick (View view){

                    if ((!num_cuenta.getText().toString().equals("")) && (!num_referencia.getText().toString().equals(""))&& (!monto_deposito.getText().toString().equals(""))) {
                        if (cuenta_origen.isEnabled()) {
                            if ((!cuenta_origen.getText().toString().equals(""))) {

                                NetAsync(view);
                            }
                        }else{
                                NetAsync(view);
                            }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Uno de los campos esta vacio", Toast.LENGTH_SHORT).show();
                    }


            }
            });

            return view;
        }

        protected class NetCheck extends AsyncTask<String,Void,Boolean>
        {
            private ProgressDialog nDialog;
            protected void  onPreExecute(){
                super.onPreExecute();
                nDialog = new ProgressDialog(getActivity());
                nDialog.setTitle("Checking Network");
                nDialog.setMessage("Loading..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();

            }
            @Override
            protected Boolean doInBackground(String... args) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isConnected()) {
                    try {
                        URL url = new URL("http://www.google.com");
                        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                        urlc.setConnectTimeout(3000);
                        urlc.connect();
                        if (urlc.getResponseCode() == 200) {
                            return true;
                        }
                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return false;
            }

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            protected void onPostExecute(Boolean th){
                if(th == true){
                    nDialog.dismiss();
                    new ProcessRecarga().execute();
                }
                else{
                    nDialog.dismiss();

                }
            }
        }

        private class ProcessRecarga extends AsyncTask<String,Void,JSONObject> {
            /**
             * Defining Process dialog
             **/
            private ProgressDialog pDialog;
            String cuenta,monto,referencia,imei,fecha,tipo,cta_origen;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
                HashMap dato = new HashMap();
                dato = db.getUser();
                imei= dato.get("imei").toString();
                //numero_a_recargar = (EditText) view.findViewById(R.id.numero_a_recargar);
                //monto_recarga = (EditText) view.findViewById(R.id.monto_recarga);
                cuenta = num_cuenta.getText().toString();
                monto = monto_deposito.getText().toString();
                referencia = num_referencia.getText().toString();
                if(cuenta_origen.isEnabled()) {
                    tipo = "Transferencia";
                    cta_origen= cuenta_origen.getText().toString();
                }
                else{
                    tipo = "Deposito";
                    cta_origen = "No Aplica";
                }

                fecha = df1.format(c.getTime());
                pDialog = new ProgressDialog(getActivity());
                pDialog.setTitle("Contacting Servers");
                pDialog.setMessage("Registering ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            @Override
            protected JSONObject doInBackground(String... args) {
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.notificarDeposito(cuenta, imei, monto, fecha, referencia,tipo,cta_origen);
                return json;
            }
            @Override
            protected void onPostExecute(JSONObject json) {
                /**
                 * Checks for success message.
                 **/
                try {
                    if (json.getString(KEY_ERROR) != null) {
                        registerErrorMsg.setText("");
                        String red = json.getString(KEY_ERROR);
                        if(Integer.parseInt(red) == 0){
                            pDialog.dismiss();
                        registerErrorMsg.setText(json.getString("error_msg"));
                            /**
                             * Removes all the previous data in the SQlite database
                             **/

                        }
                        else if (Integer.parseInt(red) ==1){
                            pDialog.dismiss();
                            registerErrorMsg.setText(json.getString("error_msg"));
                        }

                    }
                    else{
                        pDialog.dismiss();
                        registerErrorMsg.setText("Error de Registro ");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }}
