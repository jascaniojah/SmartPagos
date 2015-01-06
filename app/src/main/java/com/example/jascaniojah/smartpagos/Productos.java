package com.example.jascaniojah.smartpagos;

/**
 * Created by root on 31/10/14.
 */


public class Productos{
    private String codigo;
    private String nombre;

public Productos(){}

public Productos(String codigo, String nombre){
        this.codigo = codigo;
        this.nombre = nombre;
        }

public void setNombre(String nombre){
        this.nombre = nombre;
        }

public void setCodigo(String codigo){
        this.codigo = codigo;
        }

public String getNombre(){
        return this.nombre;
        }

public String getCodigo(){
        return this.codigo;
        }

        }
