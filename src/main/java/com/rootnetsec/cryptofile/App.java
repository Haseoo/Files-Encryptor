package com.rootnetsec.cryptofile;

public class App {

    public static void main(String[] args) {
        byte[] ciphertext = AESGCMEncryption.encryptBytesWithAES("Hello World, I'm encrypted.".getBytes(), "qwerty");
        byte[] plaintext = AESGCMEncryption.decryptBytesWithAES(ciphertext, "qwerty");
        
        System.out.println(new String(plaintext));

    }

}