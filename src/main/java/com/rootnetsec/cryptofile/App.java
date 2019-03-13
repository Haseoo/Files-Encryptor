package com.rootnetsec.cryptofile;

public class App {

    public static void main(String[] args) {
        Crypto crypto = new Crypto();
        byte[] ciphertext = crypto.encrypt("Hello World".getBytes(), "qwerty".getBytes());
        byte[] plaintext = crypto.decrypt(ciphertext, "qwerty".getBytes());
        System.out.println(new String(plaintext));

    }

}