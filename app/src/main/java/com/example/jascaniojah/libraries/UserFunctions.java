package com.example.jascaniojah.libraries;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "http://ifweb.dlinkddns.com/test/api/login/login";
    private static String saldoURL = "http://ifweb.dlinkddns.com/test/api/ConsultaSaldo/SaldoOperador";
    private static String recargaURL = "http://ifweb.dlinkddns.com/test/api/Recarga/Recarga";
    private static String notificarURL = "http://ifweb.dlinkddns.com/test/api/NotificarDeposito/NotificarDeposito";
    private static String bancosURL =  "http://ifweb.dlinkddns.com/test/api/Bancos/Consulta_Bancos";
    private static String cuentasURL = "http://ifweb.dlinkddns.com/test/api/Cuentas/Consulta_Cuentas";
    private static String productosURL = "http://ifweb.dlinkddns.com/test/api/Productos/Consulta_Productos";
    private static String transURL = "http://ifweb.dlinkddns.com/test/api/Transacciones/Consulta_Transacciones";
        // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();

    }
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String usuario, String password, String imei, String numero, String fechahora ) throws JSONException {
        // Building Parameters
       // List params = new ArrayList();
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", numero);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        JSONObject json = jsonParser.getJSON(loginURL, json1);
        return json;
    }

    public JSONObject getBancos(String telefono,String imei,String fechahora_disp, String usuario,String password) throws JSONException {
        JSONObject json1 = new JSONObject();

        json1.accumulate("telefono", telefono);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora_disp);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);


        JSONObject json = jsonParser.getJSON(bancosURL, json1);
        return json;
    }

    public JSONObject getCuentas(String telefono,String imei,String fechahora_disp,String codigo, String usuario, String password) throws JSONException {
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", telefono);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora_disp);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        json1.accumulate("codigo_banco",codigo);

        JSONObject json = jsonParser.getJSON(cuentasURL, json1);
        return json;
    }

    public JSONObject getSaldo(String usuario, String password, String imei, String numero, String fechahora ) throws JSONException {
        // Building Parameters
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", numero);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);

        JSONObject json = jsonParser.getJSON(saldoURL, json1);
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

    public JSONObject registrarVenta(String usuario, String imei, String monto, String fechahora, String numero, String producto, String password, String telefono) throws JSONException {
        // Building Parameters
        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefonorecarga", numero);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        json1.accumulate("producto",producto);
        json1.accumulate("modopago","01");
        json1.accumulate("mediopago","04");
        json1.accumulate("monto",monto);
        json1.accumulate("telefono",telefono);
        json1.accumulate("trace",100000+rnd.nextInt(900000));

        JSONObject json = jsonParser.getJSON(recargaURL, json1);
        return json;
    }

    public JSONObject notificarDeposito(String cuenta, String imei, String monto, String fechahora, String referencia, String tipo,String banco,String usuario, String password, String fechadep, String numero) throws JSONException {
        // Building Parameters
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", numero);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp",fechahora);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        json1.accumulate("cuenta",cuenta);
        json1.accumulate("referencia",referencia);
        json1.accumulate("tipo",tipo);
        json1.accumulate("fecha_deposito",fechadep);
        json1.accumulate("monto",monto);
        JSONObject json = jsonParser.getJSON(notificarURL, json1);
        return json;
    }
    public JSONObject getTransacciones(String telefono, String imei, String fechahora_disp, String fechainicio, String fechafin,String usuario,String password ) throws JSONException {
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", telefono);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora_disp);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        json1.accumulate("fechainicio", fechainicio);
        json1.accumulate("fechafin", fechafin);
        JSONObject json=jsonParser.getJSON(transURL, json1);
        return json;

    }

    public JSONObject getProductos(String telefono,String imei,String fechahora_disp,String usuario, String password) throws JSONException {
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", telefono);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora_disp);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        JSONObject json = jsonParser.getJSON(productosURL, json1);
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
