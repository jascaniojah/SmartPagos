package com.example.jascaniojah.smartpagos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class Registro extends Activity {
    /**
     *  JSON Response node names.
     **/
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";
    /**
     * Defining layout items.
     **/
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg,Nombre,Apellido,Usuario,Email,Password;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /**
         * Defining all layout items
         **/
        inputFirstName = (EditText) findViewById(R.id.editnombre);
        inputLastName = (EditText) findViewById(R.id.editapellido);
        inputUsername = (EditText) findViewById(R.id.edituser);
        inputEmail = (EditText) findViewById(R.id.editemail);
        inputPassword = (EditText) findViewById(R.id.editpword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        Nombre = (TextView) findViewById(R.id.nombre);
        Apellido = (TextView) findViewById(R.id.apellido);
        Email = (TextView) findViewById(R.id.email);
        Usuario = (TextView) findViewById(R.id.user);
        Password = (TextView) findViewById(R.id.passw);

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) && ( !inputFirstName.getText().toString().equals("")) && ( !inputLastName.getText().toString().equals("")) && ( !inputEmail.getText().toString().equals("")) )
                {
                    if ( inputUsername.getText().toString().length() > 4 ){
                        NetAsync(view);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Su Usuario deberia tener un minimo de 5 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Uno de los campos requeridos esta VACIO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Async Task to check whether internet connection is working
     **/
    private class NetCheck extends AsyncTask<String,Void,Boolean>
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Registro.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... args){
/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
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
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }
    private class ProcessRegister extends AsyncTask <String,Void,JSONObject>{
        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;
        String email,password,fname,lname,uname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.edituser);
            inputPassword = (EditText) findViewById(R.id.editpword);
            fname = inputFirstName.getText().toString();
            lname = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            uname= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Registro.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.registerUser(fname, lname, email, uname, password);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);
                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");
                        registerErrorMsg.setText("Successfully Registered");
                        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Removes all the previous data in the SQlite database
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                        /**
                         * Stores registered data in SQlite Database
                         * Launch Registered screen
                         **/
                        Intent registered = new Intent(getApplicationContext(), LoginActivity.class);
                        /**
                         * Close all views before launching Registered screen
                         **/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);
                        finish();
                    }
                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                        registerErrorMsg.setText("Ya existe ese Usuario");
                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                        registerErrorMsg.setText("Email Invalido");
                    }
                }
                else{
                    pDialog.dismiss();
                    registerErrorMsg.setText("Error en Registro");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }}