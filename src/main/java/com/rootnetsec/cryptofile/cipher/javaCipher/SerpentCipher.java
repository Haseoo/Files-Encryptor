package com.rootnetsec.cryptofile.cipher.javaCipher;

public class SerpentCipher extends JavaCipher {
    public SerpentCipher(){
        super("SERPENT", "SERPENT/CBC/PKCS7Padding", "BC", EncryptionType.SERPENT);
    }


    public static void main(String...args) {
        SerpentCipher sp = new SerpentCipher();

        try {
            sp.encryptFile("README.md", "test.enc", "test");

        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}

