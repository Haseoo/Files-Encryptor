package com.rootnetsec.cryptofile.cipher.javaCipher;

import com.rootnetsec.cryptofile.cipher.javaCipher.JavaSTDCipher;

public final class Twofish extends JavaSTDCipher {
    public Twofish(){
        super("TWOFISH", "TWOFISH/CBC/PKCS7Padding", "BC", EncryptionType.TWOFISH);
    }


}
