package com.example.jascaniojah.libraries;

import android.content.Context;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "http://www.tufuturo.com.ve/smartpagos_webs/";
    //private static String registerURL = "http://smartpagos.webege.com/";
    private static String login_tag = "login";
    //private static String register_tag = "register";
    private static String saldoURL = "http://www.tufuturo.com.ve/smartpagos_webs/";
    private static String saldo_tag = "consulta";
    private static String recargaURL = "http://www.tufuturo.com.ve/smartpagos_webs/";
    private static String recarga_tag = "recarga";
    private static String notificar_tag = "notificacion";
    private static String notificarURL = "http://www.tufuturo.com.ve/smartpagos_webs/";
    private static String bancosURL =  "http://www.tufuturo.com.ve/smartpagos_webs/";
    private static String bancos_tag = "bancos";
    private static String cuentas_tag = "cuentas";
    private static String productos_tag = "productos";
    private static String URL =  "http://www.tufuturo.com.ve/smartpagos_webs/";
    private static String transacciones_tag="transacciones";

        // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String usuario, String password, String imei, String numero){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("usuario", usuario));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("imei", imei));
        params.add(new BasicNameValuePair("numero", numero));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    public JSONObject getBancos(String telefono,String imei,String fechahora_disp, String usuario,String password){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", bancos_tag));
        params.add(new BasicNameValuePair("telefono",telefono));
        params.add(new BasicNameValuePair("imei", imei));
        params.add(new BasicNameValuePair("fechahora", fechahora_disp));
        params.add(new BasicNameValuePair("servicio", "001"));
        params.add(new BasicNameValuePair("origen", "004"));
        params.add(new BasicNameValuePair("usuario",usuario ));
        params.add(new BasicNameValuePair("password", password));

        JSONObject json = jsonParser.getJSONFromUrl(notificarURL,params);
        return json;
    }

    public JSONObject getCuentas(String telefono,String imei,String fechahora_disp,String codigo, String usuario, String password){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", cuentas_tag));
        params.add(new BasicNameValuePair("codigo_banco",codigo));
        params.add(new BasicNameValuePair("telefono",telefono));
        params.add(new BasicNameValuePair("imei", imei));
        params.add(new BasicNameValuePair("fechahora", fechahora_disp));
        params.add(new BasicNameValuePair("servicio", "001"));
        params.add(new BasicNameValuePair("origen", "004"));
        params.add(new BasicNameValuePair("usuario",usuario ));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(notificarURL,params);
        return json;
    }

    public JSONObject getSaldo(String usuario, String imei,String password){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", saldo_tag));
        params.add(new BasicNameValuePair("usuario", usuario));
        params.add(new BasicNameValuePair("imei", imei));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    /**
     * Function to change password
     **/

    /**
     * Function to reset the password
     **/

    /**
     * Function to  Register
     **/

    public JSONObject registrarVenta(String usuario, String imei, String monto, String fechahora, String telefono, String producto, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", recarga_tag));
        params.add(new BasicNameValuePair("usuario", usuario));
        params.add(new BasicNameValuePair("imei", imei));
        params.add(new BasicNameValuePair("monto", monto));
        params.add(new BasicNameValuePair("fechahora", fechahora));
        params.add(new BasicNameValuePair("telefono", telefono));
        params.add(new BasicNameValuePair("producto", producto));
        params.add(new BasicNameValuePair("modo_pago", "01"));
        params.add(new BasicNameValuePair("medio_pago", "04"));
        params.add(new BasicNameValuePair("usuario",usuario ));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(recargaURL,params);
        return json;
    }

    public JSONObject notificarDeposito(String cuenta, String imei, String monto, String fechahora, String referencia, String tipo,String banco,String usuario, String password, String fechadep){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", notificar_tag));
        params.add(new BasicNameValuePair("cuenta_id", cuenta));
        params.add(new BasicNameValuePair("imei", imei));
        params.add(new BasicNameValuePair("monto", monto));
        params.add(new BasicNameValuePair("fechahora", fechahora));
        params.add(new BasicNameValuePair("referencia", referencia));
        params.add(new BasicNameValuePair("tipo_deposito", tipo));
        params.add(new BasicNameValuePair("banco", banco));
        params.add(new BasicNameValuePair("usuario",usuario ));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("fecha_deposito",fechadep));
        JSONObject json = jsonParser.getJSONFromUrl(notificarURL,params);
        return json;
    }
    public JSONObject getTransacciones(String telefono,String servicio, String origen, String imei, String fechahora, String fechainicio, String fechafin,String usuario,String password )
    {

        List params=new ArrayList();
        params.add(new BasicNameValuePair("tag",transacciones_tag));
        params.add(new BasicNameValuePair("telefono",telefono));
        params.add(new BasicNameValuePair("servicio",servicio));
        params.add(new BasicNameValuePair("origen",origen));
        params.add(new BasicNameValuePair("imei",imei));
        params.add(new BasicNameValuePair("fechahora",fechahora));
        params.add(new BasicNameValuePair("fechainicio",fechainicio));
        params.add(new BasicNameValuePair("fechafin",fechafin));
        params.add(new BasicNameValuePair("usuario",usuario ));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json=jsonParser.getJSONFromUrl(URL,params);
        return json;

    }

    public JSONObject getProductos(String telefono,String imei,String fechahora_disp,String usuario, String Password){
    List params = new ArrayList();
    params.add(new BasicNameValuePair("tag", productos_tag));
    params.add(new BasicNameValuePair("telefono",telefono));
    params.add(new BasicNameValuePair("imei", imei));
    params.add(new BasicNameValuePair("fechahora", fechahora_disp));
    params.add(new BasicNameValuePair("servicio", "001"));
    params.add(new BasicNameValuePair("origen", "004"));
    JSONObject json = jsonParser.getJSONFromUrl(notificarURL,params);
    return json;
}
    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DataBaseHandler db = new DataBaseHandler(context);
        db.resetTables();
        return true;
    }
}
