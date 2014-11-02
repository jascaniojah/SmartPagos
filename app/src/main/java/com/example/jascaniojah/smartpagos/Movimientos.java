package com.example.jascaniojah.smartpagos;

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


    public Movimientos(Date fechaHora, String telefono, float monto) {
        FechaHora = fechaHora;
        Telefono = telefono;
        Monto = monto;
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
