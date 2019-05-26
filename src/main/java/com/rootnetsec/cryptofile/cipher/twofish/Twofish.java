package com.rootnetsec.cryptofile.cipher.twofish;

import com.rootnetsec.cryptofile.TwofishCipher;
import com.rootnetsec.cryptofile.cipher.Cipher;

public class Twofish extends Cipher {
    public Twofish() {
        super();
    }
    @Override
    public void decryptFile(String srcFile, String destFile, String userKey) {
        TwofishCipher.decryptFile(srcFile, destFile, userKey);
    }
    @Override
    public void encryptFile(String srcFile, String destFile, String userKey) {
        TwofishCipher.encryptFile(srcFile, destFile, userKey);
    }


}
