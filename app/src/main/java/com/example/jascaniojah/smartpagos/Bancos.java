package com.example.jascaniojah.smartpagos;

/**
 * Created by root on 24/10/14.
 */
public class Bancos {

    private String nombre;
    private String codigo;

    public Bancos(){}

    public Bancos(String nombre, String codigo){
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public void setBanco(String nombre){
        this.nombre = nombre;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public String getBanco(){
        return this.nombre;
    }

    public String getCodigo(){
        return this.codigo;
    }

}
