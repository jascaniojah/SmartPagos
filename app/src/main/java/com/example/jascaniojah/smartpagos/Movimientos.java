package com.example.jascaniojah.smartpagos;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Felix on 10/31/2014.
 */
public class Movimientos {

    private Date FechaHora;	//FechaHora de la transacción
    private String TipoRecarga ; 		//Tipo de recarga
    private String Serial;		//Serial o código de recarga
    private float Monto ;		// Monto de la transacción
    private String idRespuesta ;		//Id de respuesta
    private String Telefono;
    private String DesRespuesta;	//Descripción  de respuesta

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    private String Producto;

    public Movimientos( String telefono, float monto,Date fechaHora, String producto) {
        FechaHora = fechaHora;
        Telefono = telefono;
        Monto = monto;
        Producto=producto;
    }


    public Movimientos(Date fechaHora, String tipoRecarga, String serial, float monto, String idRespuesta, String desRespuesta,String telefono) {
        FechaHora = fechaHora;
        TipoRecarga = tipoRecarga;
        Serial = serial;
        Monto = monto;
        Telefono=telefono;
        this.idRespuesta = idRespuesta;
        DesRespuesta = desRespuesta;
    }

    public Movimientos(JSONObject object){
        Log.i("Movimientos","JSON object gotten "+object);

        try {
            String fechaString=object.getString("fechahora");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
           // this.FechaHora= dateFormat.parse(fechaString);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse("21/12/2012");
            FechaHora=d;

            this.Telefono = object.getString("numero");
           // this.Serial=object.getString("id");
            this.Monto=object.getLong("monto");
            this.Monto=object.getInt("monto");
            Log.i("Movimientos","Monto int "+ Monto);
            Log.i("Movimientos","Monto String "+  object.getString("monto"));

            Log.i("Movimientos.javac"," Telefono gotten JSONObject: "+object.getString("numero"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("Movimientos.java"," Telefono: "+Telefono);

    }

    public static ArrayList<Movimientos> fromJson(JSONArray jsonObjects) {
        Log.i("Movimientos.java","JSONArray: "+jsonObjects.toString());

        ArrayList<Movimientos> users = new ArrayList<Movimientos>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
               // Movimientos mov=new Movimientos(jsonObjects.getJSONObject(i).getString("numero"));
                users.add(new Movimientos(jsonObjects.getJSONObject(i)));
                Log.i("Movimientos.java","Movimientos Element Sent to Object: "+jsonObjects.getJSONObject(i).toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }



    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public Date getFechaHora() {
        return FechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        FechaHora = fechaHora;
    }

    public String getTipoRecarga() {
        return TipoRecarga;
    }

    public void setTipoRecarga(String tipoRecarga) {
        TipoRecarga = tipoRecarga;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public float getMonto() {
        return Monto;
    }

    public void setMonto(float monto) {
        Monto = monto;
    }

    public String getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(String idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getDesRespuesta() {
        return DesRespuesta;
    }

    public void setDesRespuesta(String desRespuesta) {
        DesRespuesta = desRespuesta;
    }
}
