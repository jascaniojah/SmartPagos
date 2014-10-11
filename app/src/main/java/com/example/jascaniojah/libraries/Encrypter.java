package com.example.jascaniojah.libraries;

public class Encrypter {

    public static void main(String [] args)
    {
        SecurityFunctions sf=new SecurityFunctions("0e2ec11cdf82fa49b5c35dfd9d6a654923ee36db","72355628");
        String text="123456";
        String encoded=sf.encrypt(text);
        String decoded=sf.decrypt(encoded);
        System.out.println("Original "+text);
        System.out.println("Encoded "+encoded);
        System.out.println("Decoded "+decoded);
    }

}

