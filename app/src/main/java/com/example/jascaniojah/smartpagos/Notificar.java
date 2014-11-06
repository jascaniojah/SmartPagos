package com.example.jascaniojah.smartpagos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class Notificar extends Fragment {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
    private static String KEY_ERROR = "error";
    private ArrayList<Bancos> banksList;
    private ArrayList<Cuentas> cuentasList;
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    TextView cuenta, referencia, monto,origen,banco,registerErrorMsg;
    EditText num_referencia, monto_deposito,cuenta_origen;
    Button boton_notificar;

    Spinner spnr,spinner,spinnerCta;
    String mBanco,mCuenta,codigo,numero,imei,fecha,usuario;
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
        num_referencia = (EditText) view.findViewById(R.id.num_referencia);
        monto_deposito = (EditText) view.findViewById(R.id.monto_deposito);
        monto_deposito.addTextChangedListener(tw);
        origen = (TextView) view.findViewById(R.id.origen);
        cuenta_origen = (EditText) view.findViewById(R.id.cuenta_origen);
        boton_notificar = (Button) view.findViewById(R.id.boton_notificar);
        banco = (TextView) view.findViewById(R.id.banco);
           banksList = new ArrayList<Bancos>();
        cuentasList = new ArrayList<Cuentas>();
           spnr = (Spinner) view.findViewById(R.id.spinner);
        spinner = (Spinner) view.findViewById(R.id.BancoSp);
        spinnerCta= (Spinner) view.findViewById(R.id.CuentaSp);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, caso);

        new getBancos().execute();

        spnr.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mBanco =spinner.getSelectedItem().toString();
                codigo = banksList.get(spinner.getSelectedItemPosition()).getCodigo();
                cuentasList.clear();
                new getCuentas().execute();
              }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mCuenta =spinnerCta.getSelectedItem().toString();
                            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                    };
                                @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );




        boton_notificar.setOnClickListener(new View.OnClickListener() {
            @Override
                            public void onClick (View view){

                    if ((!num_referencia.getText().toString().equals(""))&& (!monto_deposito.getText().toString().equals(""))) {
                        if (cuenta_origen.isEnabled()) {
                            if ((!cuenta_origen.getText().toString().equals(""))) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Confirma Registro de Pago?")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                NetAsync();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                                            }else{
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Falta Numero de Cuenta de donde Transfiere", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Confirma Registro de Pago?")
                                    .setCancelable(false)
                                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            NetAsync();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Uno de los campos esta vacio", Toast.LENGTH_SHORT).show();
                    }


            }
            });

            return view;
        }

    private class getBancos extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando Informacion Bancaria");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            UserFunctions jsonParser = new UserFunctions();
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            numero=telephonyManager.getLine1Number().toString();
            DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
            HashMap cuenta = new HashMap();
            cuenta = db.getUser();
            usuario = cuenta.get("usuario").toString();
            imei= cuenta.get("imei").toString();
            fecha= df3.format(c.getTime());
            JSONObject json = jsonParser.getBancos(numero,imei,fecha);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {

                    if (json != null) {
                        JSONArray banks = json
                                .getJSONArray("bancos");

                        for (int i = 0; i < banks.length(); i++) {
                            JSONObject catObj = (JSONObject) banks.get(i);
                            Bancos cat = new Bancos(catObj.getString("banco"),
                                    catObj.getString("codigo"));
                            banksList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < banksList.size(); i++) {
            lables.add(banksList.get(i).getBanco());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);
    }

    private class getCuentas extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando Informacion Bancaria");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            UserFunctions jsonParser = new UserFunctions();
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            numero=telephonyManager.getLine1Number().toString();
            DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
            HashMap cuenta = new HashMap();
            cuenta = db.getUser();
            usuario = cuenta.get("usuario").toString();
            imei= cuenta.get("imei").toString();
            fecha= df3.format(c.getTime());
            JSONObject json = jsonParser.getCuentas(numero, imei, fecha, codigo);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {

                    if (json != null) {
                        JSONArray banks = json
                                .getJSONArray("cuentas");

                        for (int i = 0; i < banks.length(); i++) {
                            JSONObject catObj = (JSONObject) banks.get(i);
                            Cuentas cat = new Cuentas(catObj.getString("numero_cuenta"));
                            cuentasList.add(cat);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            llenarSpinner();
        }

    }

    /**
     * Adding spinner data
     * */
    private void llenarSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < cuentasList.size(); i++) {
            lables.add(cuentasList.get(i).getNumero());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCta.setAdapter(spinnerAdapter);
    }



    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, ',');

                monto_deposito.removeTextChangedListener(this);
                monto_deposito.setText(cashAmountBuilder.toString());

                monto_deposito.setTextKeepState("BsF." + cashAmountBuilder.toString());
                Selection.setSelection(monto_deposito.getText(), cashAmountBuilder.toString().length() + 1);

                monto_deposito.addTextChangedListener(this);
            }
        }
    };

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
            String monto,referencia,imei,fecha,tipo,cta_origen;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
                HashMap dato = new HashMap();
                dato = db.getUser();
                imei= dato.get("imei").toString();
                //numero_a_recargar = (EditText) view.findViewById(R.id.numero_a_recargar);
                //monto_recarga = (EditText) view.findViewById(R.id.monto_recarga);
                monto = monto_deposito.getText().toString().replace("BsF.", "");
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
                JSONObject json = userFunction.notificarDeposito(mCuenta, imei, monto, fecha, referencia,tipo,cta_origen,mBanco);
                return json;
            }
            @Override
            protected void onPostExecute(JSONObject json) {
                /**
                 * Checks for success message.
                 **/
                try {
                    if (json.getString(KEY_ERROR) != null) {

                        String red = json.getString(KEY_ERROR);
                        if(Integer.parseInt(red) == 0){
                            num_referencia.getText().clear();
                            monto_deposito.getText().clear();
                            if(cuenta_origen.isEnabled()){
                                cuenta_origen.getText().clear();
                            }
                            pDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            /**
                             * Removes all the previous data in the SQlite database
                             **/

                        }
                        else if (Integer.parseInt(red) ==1){
                            pDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        pDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Error de Registro", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}
    public void NetAsync(){
        new NetCheck().execute();
    }}
