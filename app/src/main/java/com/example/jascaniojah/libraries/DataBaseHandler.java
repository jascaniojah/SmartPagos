package com.example.jascaniojah.libraries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DataBaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "smartpagos";
    // Login table name
    private static final String TABLE_CUENTA = "cuenta";
    // Login Table Columns names
    private static final String KEY_UID = "uid";
    private static final String KEY_TLF = "telefono";
    private static final String KEY_IMEI = "imei";
    private static final String KEY_FECHA_DISP = "fechahora_disp";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER = "usuario";
    private static final String KEY_FECHA_SERV = "fechahora_server";
    private static final String KEY_FECHA_TRANS = "fechahora_trans";
    private static final String KEY_SALDO = "saldo";
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CUENTA_TABLE = "CREATE TABLE " + TABLE_CUENTA + "("
                + KEY_UID + "INTEGER PRIMARY KEY,"
                + KEY_TLF + " TEXT UNIQUE,"
                + KEY_IMEI + " TEXT UNIQUE,"
                + KEY_FECHA_DISP + " DATETIME,"
                + KEY_PASSWORD + " TEXT ,"
                + KEY_USER + " TEXT,"
                + KEY_FECHA_SERV + " DATETIME,"
                + KEY_FECHA_TRANS + " DATETIME ,"
                + KEY_SALDO + " FLOAT "+ ")";
        db.execSQL(CREATE_CUENTA_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUENTA);
        // Create tables again
        onCreate(db);
    }
    /**
     * Storing user details in database
     * */
    public void addUser(String telefono, String imei,String fecha_server, String saldo, String fecha_trans, String user) {
      SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Identificador
        values.put(KEY_IMEI, imei); // IMEI
        values.put(KEY_TLF, telefono); // Telefono
        values.put(KEY_FECHA_SERV, fecha_server); // Fecha servidor
      values.put(KEY_FECHA_TRANS, fecha_trans); // Fecha trans
        values.put(KEY_SALDO, saldo);
         values.put(KEY_USER,user);// Saldo
       //  Inserting Row
        db.insert(TABLE_CUENTA, null, values);
        db.close(); // Closing database connection
    }
    /**
     * Getting user data from database
     * */

    public HashMap getUserDetails(){
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_CUENTA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("usuario", cursor.getString(5));
            user.put("imei", cursor.getString(2));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }


     public void setSaldo(String usuario, String fecha_server, String fecha_trans, String saldo ){

        SQLiteDatabase db = this.getWritableDatabase();
        // Move to first row
        ContentValues values = new ContentValues();
        values.put(KEY_FECHA_SERV, fecha_server); // Fecha servidor
        values.put(KEY_FECHA_TRANS, fecha_trans); // Fecha trans
        values.put(KEY_SALDO, saldo);
        db.update(TABLE_CUENTA,values, KEY_USER+ " = '" + usuario +"'" ,null);
db.close();
    }
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CUENTA;
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }
    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CUENTA, null, null);
        db.close();
    }
}