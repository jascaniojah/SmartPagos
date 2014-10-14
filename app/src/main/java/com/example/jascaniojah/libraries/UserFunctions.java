package com.example.jascaniojah.libraries;

import android.content.Context;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "";
    private static String registerURL = "http://smartpagos.webege.com/";
    private static String login_tag = "login";
    private static String register_tag = "register";

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
    /**
     * Function to change password
     **/

    /**
     * Function to reset the password
     **/

    /**
     * Function to  Register
     **/

    public JSONObject registerUser(String fname, String lname, String email, String uname, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
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
