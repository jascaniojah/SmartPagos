package com.example.jascaniojah.smartpagos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jascaniojah.libraries.DataBaseHandler;
import com.example.jascaniojah.libraries.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;



public class CalcularPin extends Fragment {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
    private static String KEY_ERROR = "Codigo";
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    TextView montopin,iva,total,comision,neto;
    EditText cantpin;
    Button boton_calcular,boton_continuar;
    String fecha,imei,numero;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.calculo_pin, container, false);

        montopin= (TextView) view.findViewById(R.id.resp_monto_pin);
        iva = (TextView) view.findViewById(R.id.resp_iva);
        total = (TextView) view.findViewById(R.id.resp_total);
        comision= (TextView) view.findViewById(R.id.resp_comision);
        neto= (TextView) view.findViewById(R.id.resp_neto);
        boton_calcular= (Button) view.findViewById(R.id.boton_calcular);
        boton_continuar= (Button) view.findViewById(R.id.boton_siguiente);
        cantpin= (EditText) view.findViewById(R.id.cant_pin);
        boton_continuar.setEnabled(false);

        boton_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                if (!cantpin.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Desea calcular el Monto?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    boton_continuar.setEnabled(true);
                                    //   new ProcessCalcular();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ingrese cantidad de pines", Toast.LENGTH_SHORT).show();
                }


            }
        });

        boton_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.layout.notificar_frag, new Notificar(), "fragment_screen");
                ft.commit();
            }


        });
    return view;
    }
    private class ProcessCalcular extends AsyncTask<String,Void,JSONObject> {
        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;
        String pines;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pines = cantpin.getText().toString();
            fecha = df3.format(c.getTime());
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contactando Servidores");
            pDialog.setMessage("Registrando ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
            HashMap dato = new HashMap();
            dato = db.getUser();
            numero= dato.get("telefono").toString();
            imei= dato.get("imei").toString();
            String usuario=dato.get("usuario").toString();
            String password=dato.get("password").toString();
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = null;
            try {
                json = userFunction.calculoPin(usuario,password, imei, numero, fecha, pines );
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    if(Integer.parseInt(red) == 000){
                        //fechapicker.getText().clear();
                        pDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(json.getString("Descripcion_codigo")+'\n'+"Numero de pedido: "+json.getString("pedido"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        /**
                         * Removes all the previous data in the SQlite database
                         **/

                    }
                    else if (Integer.parseInt(red) !=000){
                        pDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(json.getString("Descripcion_codigo"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }

                }
                else{
                    pDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Error de Registro")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}




