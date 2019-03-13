package com.rootnetsec.cryptofile;

public class App {

    public static void main(String[] args) {
        Crypto crypto = new Crypto();
        byte[] ciphertext = crypto.EncryptBytes("Hello World".getBytes(), "qwerty");
        byte[] plaintext = crypto.DecryptBytes(ciphertext, "qwerty");
        
        System.out.println(new String(plaintext));

    }

}