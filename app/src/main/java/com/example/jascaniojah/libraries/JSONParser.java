package com.example.jascaniojah.libraries;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static String chain = "";

    // constructor
    public JSONParser() {
    }

    public JSONObject getJSON(String ur, JSONObject content){

        try{

            // 1. URL
            URL url = new URL(ur);

            // 2. Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            // 3. Specify POST method
            conn.setRequestMethod("POST");

            // 4. Set the headers
            conn.setRequestProperty("Content-Type", "application/json");


            conn.setDoOutput(true);

            // 5. Add JSON data into POST request body

            //`5.1 Use Jackson object mapper to convert Contnet object into JSON
            //ObjectMapper mapper = new ObjectMapper();

            // 5.2 Get connection output stream

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream ());
            printout.writeBytes(content.toString());
            Log.e("POST",content.toString());
            printout.flush();
            printout.close ();
            // 6. Get the response
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 7. Print result
            json = response.toString();

            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            // return JSON String
            Log.e("respuesta",response.toString());


        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch(SocketTimeoutException g) {
            try {
                jObj = new JSONObject();
                jObj.put("Codigo", "902");
                jObj.put("Descripcion_codigo", "FUERA DE SERVICIO, INTENTE MAS TARDE");

            } catch (JSONException f) {
                Log.e("JSON Parser", "Error parsing data " + f.toString());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return jObj;

    }



    }



