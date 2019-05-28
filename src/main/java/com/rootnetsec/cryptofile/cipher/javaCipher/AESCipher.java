package com.rootnetsec.cryptofile.cipher.javaCipher;
import com.rootnetsec.cryptofile.cipher.javaCipher.JavaSTDCipher;

public final class AESCipher extends JavaSTDCipher {
    public AESCipher(){
        super("AES", "AES/CBC/PKCS5PADDING", "BC");

    }
}