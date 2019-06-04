package com.rootnetsec.cryptofile.cipher.javaCipher;

public final class TwofishCipher extends JavaCipher {
    public TwofishCipher(){
        super("TWOFISH", "TWOFISH/CBC/PKCS7Padding", "BC", EncryptionType.TWOFISH);
    }


}
