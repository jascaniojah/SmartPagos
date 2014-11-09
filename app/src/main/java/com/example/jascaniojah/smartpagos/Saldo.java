package com.example.jascaniojah.smartpagos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jascaniojah.libraries.DataBaseHandler;
import com.example.jascaniojah.libraries.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;



public class Saldo extends Fragment {
    private static String KEY_SUCCESS = "success";
    private static String KEY_IMEI = "imei";
    private static String KEY_USER = "usuario";
    private static String KEY_FECHA_SERV = "fecha_server";
    private static String KEY_FECHA_TRANS = "fecha_trans";
    private static String KEY_SALDO = "saldo";
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    TextView fecha_consulta,hora_consulta,fecha_ult_trans,hora_ult_trans,saldo_actual;
    TextView resp_fecha_consulta,resp_hora_consulta,resp_fecha_ult_trans,resp_hora_ult_trans,resp_saldo_actual;
    Button saldo_boton;
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    DecimalFormat decimalFormat = new DecimalFormat();
        @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saldo_frag, container, false);
        fecha_consulta= (TextView) view.findViewById(R.id.fecha_consulta);
        fecha_ult_trans= (TextView)view.findViewById(R.id.fecha_ult_trans);
        saldo_actual= (TextView)view.findViewById(R.id.saldo);
        resp_fecha_consulta=(TextView)view.findViewById(R.id.resp_fecha_consulta);
        resp_fecha_ult_trans=(TextView)view.findViewById(R.id.resp_fecha_ult_trans);
        resp_saldo_actual=(TextView)view.findViewById(R.id.resp_saldo_actual);
        saldo_boton = (Button) view.findViewById(R.id.boton_saldo);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

            DecimalFormat decimalFormat = new DecimalFormat("$ #,###.00", symbols);

            saldo_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent i = new Intent(getApplicationContext(), Principal.class);
                //startActivity(i);
                //attemptLogin();
                NetAsync(view);

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
        nDialog.setTitle("Chequeando Conexion");
        nDialog.setMessage("Cargando..");
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
            new ProcessSaldo().execute();
        }
        else{
            nDialog.dismiss();

        }
    }
}
/**
 * Async Task to get and send data to My Sql database through JSON respone.
 **/
private class ProcessSaldo extends AsyncTask <String,Void,JSONObject> {
    private ProgressDialog pDialog;

    String usuario,imei,fechaultimaTrans;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle("Contactando Servidores");
        pDialog.setMessage("Cargando..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    protected JSONObject doInBackground(String... args) {
        DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
        HashMap cuenta = new HashMap();
        cuenta = db.getUser();
        usuario = cuenta.get("usuario").toString();
        imei = cuenta.get("imei").toString();
        String password = cuenta.get("password").toString();
        UserFunctions userFunction = new UserFunctions();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        String currentDate = sdf.format(new Date());
        JSONObject json = userFunction.getSaldo(usuario, imei, password);
        JSONObject ultimaTrans = userFunction.getTransacciones("", "004", "004", imei, currentDate, "1970-01-01", currentDate, usuario, password);

        if (!ultimaTrans.equals(""))
        {

            try {
                JSONArray trans = ultimaTrans.getJSONArray("transacciones");
                Log.i("Saldo.java","Array ultima Transaccion "+trans);

                fechaultimaTrans=trans.getJSONObject(trans.length()-1).getString("fecha_hora");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

            return json;
    }

    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
                    pDialog.setTitle("Obteniendo Data");
                    DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
                    JSONObject json_user = json.getJSONObject("cuenta");
                    /**
                     * Clear all previous data in SQlite database.
                     **/
                    Double numero = Double.valueOf(json_user.getString(KEY_SALDO));
                    String saldo = decimalFormat.format(numero);
                    resp_fecha_consulta.setText((CharSequence) df1.format(c.getTime()));
                    resp_saldo_actual.setText((CharSequence) "BsF. "+ saldo);
                    resp_fecha_ult_trans.setText((CharSequence) json_user.getString(KEY_FECHA_TRANS));
                    fecha_ult_trans.setText("Fecha de Ult. Transaccion: "+fechaultimaTrans);
                    Log.i("Saldo.java","Fecha ultima Transaccion "+fechaultimaTrans);

                    db.setSaldo(usuario,json_user.getString(KEY_FECHA_SERV),json_user.getString(KEY_FECHA_TRANS),json_user.getString(KEY_SALDO));
                    /**
                     *If JSON array details are stored in SQlite it launches the User Panel.
                     **/

                                    pDialog.dismiss();
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

}

