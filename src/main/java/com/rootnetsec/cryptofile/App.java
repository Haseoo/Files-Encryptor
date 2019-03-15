package com.rootnetsec.cryptofile;

public class App {

    public static void main(String[] args) {
        byte[] ciphertext = AESGCMEncryption.encryptBytesWithAES("Hello World, I'm encrypted.".getBytes(), "qwerty");
        byte[] plaintext = AESGCMEncryption.decryptBytesWithAES(ciphertext, "qwerty");
        
        Boolean find = PasswordService.searchHaveIBeenPwnedDatabase("qwerty");
        if (find == true) {
            System.out.println("Password found in HaveIBeenPwned database!");
        } else {
            System.out.println("You're safe! For now...");
        }

        System.out.println(new String(plaintext));

    }

}