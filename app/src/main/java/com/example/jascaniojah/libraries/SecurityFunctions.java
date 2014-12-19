package com.example.jascaniojah.libraries;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class SecurityFunctions {

    public KeySpec keySpec;
    public SecretKey key;
    public IvParameterSpec iv;

    public SecurityFunctions(String keyString) {
        try {
            final byte[] digestOfPassword=hexStringToByteArray(keyString);
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            keySpec = new DESKeySpec(keyBytes);

            key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public  String encrypt(String value) {

        try {
            Cipher ecipher = Cipher.getInstance("DES/ECB/NoPadding");
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            if(value==null){
                return null;}
            String formatedValue=formatString(value);
            // Encode the string into bytes using utf-8

            byte[] hex = hexStringToByteArray(formatedValue);

            byte[] enc = ecipher.doFinal(hex);

            // Encode bytes to base64 to get a string
           // return new String(Base64.encodeBase64(enc),"HEX");
            return byteArrayToHex(enc).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

       return null;
    }

    public String decrypt(String value) {
        try {
            Cipher dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding","SunJCE");
            dcipher.init(Cipher.DECRYPT_MODE, key,iv);

            if(value==null)
                return null;

            // Decode base64 to get bytes
            byte[] dec = Base64.decodeBase64(value.getBytes());

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
    public static String formatString(String s)
    {
        String parsedString=s;
        int  stringLeng=s.length();

        if (stringLeng==6)
        {   parsedString=toHex(parsedString,12);
            parsedString="2020"+parsedString;
        }
        else if(stringLeng==7) {
            parsedString=toHex(parsedString,14);
            parsedString = "20"+parsedString;
        }
        else {
            parsedString=toHex(parsedString,16);}
        return parsedString;
    }

    public static  String toHex(String s,int number ) {
        try {
            return String.format("%0"+number+"x", new BigInteger(1, s.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}

