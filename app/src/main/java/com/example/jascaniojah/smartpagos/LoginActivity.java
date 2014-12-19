package com.example.jascaniojah.smartpagos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    private static final String TAG="LoginActivity.java";
    private static String KEY_USER = "usuario";
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    private EditText Usuario;
    private EditText Password;
    private View mProgressView;
    private View mLoginFormView;
    private TextView Registro;
    private TextView loginErrorMsg;
private String mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        Usuario = (EditText) findViewById(R.id.usuario);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        Password = (EditText) findViewById(R.id.password);
        Password.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        Button Entrar = (Button) findViewById(R.id.email_sign_in_button);
        Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!Usuario.getText().toString().equals("")) && (!Password.getText().toString().equals(""))) {
                    NetAsync(view);
                } else if ((!Usuario.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Campo de password vacio", Toast.LENGTH_SHORT).show();
                } else if ((!Password.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Campo de Email vacio", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Campos de Email y Password vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Intent i = new Intent(getApplicationContext(), Principal.class);
        //startActivity(i);
        //attemptLogin();


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    protected class NetCheck extends AsyncTask<String,Void,Boolean>
{
    private ProgressDialog nDialog;
    protected void  onPreExecute(){
        super.onPreExecute();
        nDialog = new ProgressDialog(LoginActivity.this);
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
            new ProcessLogin().execute();
        }
        else{
            nDialog.dismiss();
            loginErrorMsg.setText("Error in Network Connection");
        }
    }
}
/**
 * Async Task to get and send data to My Sql database through JSON respone.
 **/



 private class ProcessLogin extends AsyncTask <String,Void,JSONObject> {
    private ProgressDialog pDialog;
    String usuario,password,imei,numero,fechahora;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SecurityFunctions securityFunctions= new SecurityFunctions("4445BBBBBBBBBBBBBBBB");
        usuario = Usuario.getText().toString();
        mPassword = Password.getText().toString();
        mPassword=securityFunctions.encrypt(mPassword);
        Log.i(TAG,"pass: "+mPassword);
        Log.i(TAG,"Usuario:  "+usuario);

        pDialog = new ProgressDialog(LoginActivity.this);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imei=telephonyManager.getDeviceId().toString();
        //imei = "IMEI00000022";
        numero=telephonyManager.getLine1Number().toString();
        //numero = "04142222222";
        if (numero==null)
        {
            numero="0000000000";
        }
        fechahora=  df3.format(c.getTime());
        Log.i(TAG,"imei: "+imei);
        Log.i(TAG,"Numero: "+numero);
        pDialog.setTitle("Conectando al servidor");
        pDialog.setMessage("Iniciando sesion ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    protected JSONObject doInBackground(String... args) {
        UserFunctions userFunction = new UserFunctions();
        JSONObject json = null;
        try {
            json = userFunction.loginUser(usuario,mPassword,imei,numero,fechahora);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getString("Codigo") != null) {
                String res = json.getString("Codigo");
                if(Integer.parseInt(res) == 000){
                    pDialog.setMessage("Cargando Interfaz de Usuario");
                    pDialog.setTitle("Obteniendo Data");
                    DataBaseHandler db = new DataBaseHandler(getApplicationContext());
                    //JSONObject json_user = json.getJSONObject("cuenta");

                    /**
                     * Clear all previous data in SQlite database.
                     **/
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    db.addUser(usuario,imei,mPassword,numero);
                    /**
                     *If JSON array details are stored in SQlite it launches the User Panel.
                     **/
                    pDialog.dismiss();
                    Intent upanel = new Intent(getApplicationContext(), Principal.class);
                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(upanel);
                    /**
                     * Close Login Screen
                     **/
                    finish();
                }else{
                    pDialog.dismiss();
                    loginErrorMsg.setText(json.getString("Descripcion_codigo"));
                    Usuario.getText().clear();
                    Password.getText().clear();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
    public void NetAsync(View view){
       new NetCheck().execute();

       };
    long lastPress;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 3000){
            Toast.makeText(getBaseContext(), "Presionar BACK de nuevo para salir", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        }else{
            super.onBackPressed();
        }
    }

}



