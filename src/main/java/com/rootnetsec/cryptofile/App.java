package com.rootnetsec.cryptofile;

public class App {

    public static void main(String[] args) {
        byte[] ciphertext = Crypto.EncryptBytesWithAES("Hello World".getBytes(), "qwerty");
        byte[] plaintext = Crypto.DecryptBytesWithAES(ciphertext, "qwerty");
        
        System.out.println(new String(plaintext));

    }

}