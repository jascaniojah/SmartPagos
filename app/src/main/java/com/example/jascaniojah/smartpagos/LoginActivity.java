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


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    private static final String TAG="LoginActivity.java";
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_TLF = "telefono";
    private static String KEY_IMEI = "imei";
    private static String KEY_FECHA_DISP = "fecha_disp";
    private static String KEY_PASSWORD = "password";
    private static String KEY_USER = "usuario";
    private static String KEY_FECHA_SERV = "fecha_server";
    private static String KEY_FECHA_TRANS = "fecha_trans";
    private static String KEY_SALDO = "saldo";

    private EditText Usuario;
    private EditText Password;
    private View mProgressView;
    private View mLoginFormView;
    private TextView Registro;
    private TextView loginErrorMsg;

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
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!Password.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
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
        nDialog.setTitle("Checking Network");
        nDialog.setMessage("Loading..");
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
    String usuario,password,imei,numero,cadena;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SecurityFunctions securityFunctions= new SecurityFunctions("0e2ec11cdf82fa49b5c35dfd9d6a654923ee36db","72355628");
        //Usuario = (EditText) findViewById(R.id.usuario);
        //Password = (EditText) findViewById(R.id.password);
        usuario = Usuario.getText().toString();
        password = Password.getText().toString();
        password=securityFunctions.encrypt(password);
        Log.i(TAG,"pass: "+password);
        pDialog = new ProgressDialog(LoginActivity.this);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imei=telephonyManager.getDeviceId().toString();
        numero=telephonyManager.getLine1Number().toString();
        if (numero==null)
        {
            numero="000000000";
        }
        Log.i(TAG,"imei: "+imei);
        Log.i(TAG,"Numero: "+numero);
        pDialog.setTitle("Contacting Servers");
        pDialog.setMessage("Logging in ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    protected JSONObject doInBackground(String... args) {
        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.loginUser(usuario,password,imei,numero);
        return json;
    }

    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getString("code") != null) {
                String res = json.getString("code");
                if(Integer.parseInt(res) == 000){
                    pDialog.setMessage("Loading User Space");
                    pDialog.setTitle("Getting Data");
                    DataBaseHandler db = new DataBaseHandler(getApplicationContext());
                    JSONObject json_user = json.getJSONObject("cuenta");
                    /**
                     * Clear all previous data in SQlite database.
                     **/
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    db.addUser(json_user.getString(KEY_USER),imei);
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
                    loginErrorMsg.setText("Incorrect username/password");
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



