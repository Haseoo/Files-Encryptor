package com.rootnetsec.cryptofile.cipher.javaCipher;

/**
 * The class that uses TWOFISH/CBC/PKCS7Padding as encryption algorithm
 */
public final class TwofishCipher extends JavaCipher {
    public TwofishCipher(){
        super("TWOFISH", "TWOFISH/CBC/PKCS7Padding", "BC", EncryptionType.TWOFISH);
    }


}
