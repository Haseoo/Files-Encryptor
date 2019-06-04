package com.rootnetsec.cryptofile.cipher.javaCipher;

public class SerpentCipher extends JavaCipher {
    public SerpentCipher(){
        super("SERPENT", "SERPENT/CBC/PKCS7Padding", "BC", EncryptionType.SERPENT);
    }
}

