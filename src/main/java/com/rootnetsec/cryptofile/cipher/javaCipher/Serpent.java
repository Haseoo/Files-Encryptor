package com.rootnetsec.cryptofile.cipher.javaCipher;
import com.rootnetsec.cryptofile.cipher.javaCipher.JavaSTDCipher;

public class Serpent extends JavaSTDCipher {
    public Serpent(){
        super("SERPENT", "Serpent/CBC/PKCS7Padding", "BC");
    }


}
