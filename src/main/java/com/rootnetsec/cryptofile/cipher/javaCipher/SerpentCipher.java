package com.rootnetsec.cryptofile.cipher.javaCipher;

/**
 * The class that uses SERPENT/CBC/PKCS7Padding as encryption algorithm
 */
public class SerpentCipher extends JavaCipher {
    public SerpentCipher(){
        super("SERPENT", "SERPENT/CBC/PKCS7Padding", "BC", EncryptionType.SERPENT);
    }
}

