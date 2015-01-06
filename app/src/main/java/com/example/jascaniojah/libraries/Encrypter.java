package com.example.jascaniojah.libraries;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class Encrypter {

    public static void main(String [] args)
    {
     //   SecurityFunctions sf=new SecurityFunctions("0e2ec11cdf82fa49b5c35dfd9d6a654923ee36db","72355628");
        SecurityFunctions sf=new SecurityFunctions("4445BBBBBBBBBBBBBBBB");
        String text="12345678";
        String formatedText=SecurityFunctions.formatString(text);
        String encoded=sf.encrypt(text);
        System.out.println("Original "+text);
        System.out.println("Formated "+formatedText);
        System.out.println("Encoded "+encoded);

    }



}

