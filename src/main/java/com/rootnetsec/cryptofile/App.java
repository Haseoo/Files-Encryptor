package com.rootnetsec.cryptofile;

public class App {

    public static void main(String[] args) {
        Crypto crypto = new Crypto();
        byte[] ciphertext = crypto.Encrypt("Hello World".getBytes(), "qwertyiopasdasdasdasdasd".getBytes());
        byte[] plaintext = crypto.Decrypt(ciphertext, "qwertyiopasdasdasdasdasd".getBytes());
        
        System.out.println(new String(plaintext));

    }

}