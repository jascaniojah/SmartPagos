package com.example.jascaniojah.smartpagos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jascaniojah.libraries.AsteriskPasswordTransformationMethod;
import com.example.jascaniojah.libraries.DataBaseHandler;
import com.example.jascaniojah.libraries.SecurityFunctions;
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


public class CambioPass extends ActionBarActivity {

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
   private EditText pass, newpass,confnewpass;
    Button boton;
    ActionBar mActionbar;
    private ImageView logo;
    // TODO: Rename and change types and number of parameters




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionbar = getSupportActionBar();
        setContentView(R.layout.fragment_cambio_pass);

        logo= (ImageView) findViewById(R.id.logo);

        pass = (EditText) findViewById(R.id.resp_clave);
        pass.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        newpass = (EditText) findViewById(R.id.resp_new_clave);
        newpass.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        confnewpass = (EditText) findViewById(R.id.resp_conf_clave);
        confnewpass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        boton = (Button) findViewById(R.id.boton_cambiar);

        mActionbar.setDisplayShowTitleEnabled(true);
        mActionbar.setTitle("Cambio de Clave");
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                if ((!pass.getText().toString().equals(""))&& (!newpass.getText().toString().equals("")) && (!confnewpass.getText().toString().equals(""))) {
                    if(newpass.getText().toString().equals(confnewpass.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CambioPass.this);
                        builder.setMessage("Confirma Cambio de Clave?")
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
                    Toast.makeText(CambioPass.this,
                            "Uno de los campos esta vacio", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    protected class NetCheck extends AsyncTask<String,Void,Boolean>
    {
        private ProgressDialog nDialog;
        protected void  onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(CambioPass.this);
            nDialog.setTitle("Chequeando Conexion");
            nDialog.setMessage("Cargando..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();

        }
        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
                new ProcessCambio().execute();
            }
            else{
                nDialog.dismiss();

            }
        }
    }

    private class ProcessCambio extends AsyncTask<String,Void,JSONObject> {
        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;
        String clave,clavenueva,imei,fechahora,numero,usuario;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SecurityFunctions securityFunctions= new SecurityFunctions("4445BBBBBBBBBBBBBBBB");
            clave = securityFunctions.encrypt(pass.getText().toString());
            clavenueva= securityFunctions.encrypt(newpass.getText().toString());
            fechahora = df3.format(c.getTime());

            pDialog = new ProgressDialog(CambioPass.this);
            pDialog.setTitle("Contactando Servidores");
            pDialog.setMessage("Registrando ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            DataBaseHandler db = new DataBaseHandler(CambioPass.this);
            HashMap dato = new HashMap();
            dato = db.getUser();
            numero= dato.get("telefono").toString();
            imei= dato.get("imei").toString();
            usuario=dato.get("usuario").toString();
            UserFunctions userFunction = new UserFunctions();

            JSONObject json = null;
            try {
                json = userFunction.cambioPass(usuario, clave, imei, numero, fechahora, clavenueva);
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
                if (json.getString("Codigo") != null) {

                    String red = json.getString("Codigo");
                    if(Integer.parseInt(red) == 000){
                        pass.getText().clear();
                        newpass.getText().clear();
                        confnewpass.getText().clear();
                        pDialog.dismiss();
                        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
                        db.resetTables();
                        db.addUser(usuario,imei,clavenueva,numero);

                        Toast.makeText(CambioPass.this,
                                json.getString("Descripcion_codigo"), Toast.LENGTH_SHORT).show();
                        /**
                         * Removes all the previous data in the SQlite database
                         **/

                    }
                    else if (Integer.parseInt(red) !=000){
                        pDialog.dismiss();
                        Toast.makeText(CambioPass.this,
                                json.getString("Descripcion_codigo"), Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    pDialog.dismiss();
                    Toast.makeText(CambioPass.this,
                            "Error de Registro", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }}
    public void NetAsync(){
        new NetCheck().execute();
    }
}



