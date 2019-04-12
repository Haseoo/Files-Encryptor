package com.rootnetsec.cryptofile;

import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        //byte[] ciphertext = AESEncryption.encryptBytes("Hello World, I'm encrypted.".getBytes(), "qwerty");
        //byte[] plaintext = AESEncryption.decryptBytes(ciphertext, "qwerty");
        
        //System.out.println(new String(plaintext));

        //Boolean find = PasswordService.searchHaveIBeenPwnedDatabase("qwerty");
        //if (find == true) {
        //    System.out.println("Password found in HaveIBeenPwned database!");
        //} else {
        //    System.out.println("You're safe! For now...");
        //}

        AesCipher.encryptFile("maslo");
        AesCipher.decryptFile("maslo");

    }


}