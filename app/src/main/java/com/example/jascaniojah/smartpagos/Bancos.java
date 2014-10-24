package com.example.jascaniojah.smartpagos;

/**
 * Created by root on 24/10/14.
 */
public class Bancos {

    private String banco;
    private String cuenta;

    public Bancos(){}

    public Bancos(String banco, String cuenta){
        this.banco = banco;
        this.cuenta = cuenta;
    }

    public void setBanco(String banco){
        this.banco = banco;
    }

    public void setCuenta(String cuenta){
        this.cuenta = cuenta;
    }

    public String getBanco(){
        return this.banco;
    }

    public String getCuenta(){
        return this.cuenta;
    }

}
