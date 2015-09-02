package com.example.jascaniojah.libraries;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "http://grupoariesco.no-ip.org:8091/api/login/login";
    private static String saldoURL = "http://grupoariesco.no-ip.org:8091/api/ConsultaSaldo/SaldoOperador";
    private static String recargaURL = "http://grupoariesco.no-ip.org:8091/api/Recarga/Recarga";
    private static String notificarURL = "http://grupoariesco.no-ip.org:8091/api/NotificarDeposito_V2/NotificarDeposito_v2";
    private static String bancosURL =  "http://grupoariesco.no-ip.org:8091/api/Bancos/Consulta_Bancos";
    private static String cuentasURL = "http://grupoariesco.no-ip.org:8091/api/Cuentas/Consulta_Cuentas";
    private static String productosURL = "http://grupoariesco.no-ip.org:8091/api/Productos/Consulta_Productos";
    private static String transURL = "http://grupoariesco.no-ip.org:8091/api/Transacciones/Consulta_Transacciones";
    private static String cambioPassURL = "http://grupoariesco.no-ip.org:8091/api/CambioClave/CambioClave";
    private static String ecURL="http://grupoariesco.no-ip.org:8091/api/Movimientos/Consulta_Movimientos";
    private static String pinURL="http://grupoariesco.no-ip.org:8091/api/CalculoMontosPin/CalculoMontosPin";
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
        //jsonParser.post(loginURL,json1);
        Log.i("LoginActivity","Json= "+json1.toString());
        return json;
    }

    public JSONObject cambioPass(String usuario, String password, String imei, String numero, String fechahora, String newpassword ) throws JSONException {
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
        json1.accumulate("newpassword", newpassword);
        JSONObject json = jsonParser.getJSON(cambioPassURL, json1);
        //jsonParser.post(loginURL,json1);
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
        addTextToFile("ARRAY JSON ENVIADO OPERACION VENTA: "+json1.toString());

        return json;
    }

    public JSONObject calculoPin(String usuario, String password, String imei, String numero, String fechahora,String pin ) throws JSONException {
        // Building Parameters
        JSONObject json1 = new JSONObject();
        json1.accumulate("telefono", numero);
        json1.accumulate("servicio", "001");
        json1.accumulate("origen", "004");
        json1.accumulate("ime", imei);
        json1.accumulate("fechahora_disp", fechahora);
        json1.accumulate("usuario", usuario);
        json1.accumulate("password", password);
        json1.accumulate("pines",pin);

        JSONObject json = jsonParser.getJSON(pinURL, json1);
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

    public JSONObject notificarDeposito2(String cuenta, String imei, String monto, String fechahora, String referencia, String tipo,String banco,String usuario, String password, String fechadep, String numero,String nominal, String venta, String iva, String descuento, String retiva, String deposito, String pines) throws JSONException {
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
        json1.accumulate("monto",monto.replaceAll("[^0-9.]", ""));
        json1.accumulate("nominal",nominal.replaceAll("[^0-9.]", ""));
        json1.accumulate("venta",venta.replaceAll("[^0-9.]", ""));
        json1.accumulate("iva",iva.replaceAll("[^0-9.]", ""));
        json1.accumulate("retiva",retiva.replaceAll("[^0-9.]", ""));
        json1.accumulate("descuento",descuento.replaceAll("[^0-9.]", ""));
        json1.accumulate("deposito",deposito.replaceAll("[^0-9.]", ""));
        json1.accumulate("pines",pines.replaceAll("[^0-9.]", ""));

        JSONObject json = jsonParser.getJSON(notificarURL, json1);
        Log.i("UserFunctions.java","JSON ARRAY NOTIFICACION: "+json1.toString());
        addTextToFile("ARRAY JSON ENVIADO OPERACION NOTIFICACION: "+json1.toString());
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

    public JSONObject getEstadodeCuenta(String telefono, String imei, String fechahora_disp, String fechainicio, String fechafin,String usuario,String password ) throws JSONException {
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
        JSONObject json=jsonParser.getJSON(ecURL, json1);
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
    public void addTextToFile(String text) {
        File logFile = new File("sdcard/" + "LOG.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
