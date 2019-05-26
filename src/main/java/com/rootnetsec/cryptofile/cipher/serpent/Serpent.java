package com.rootnetsec.cryptofile.cipher.serpent;

import com.rootnetsec.cryptofile.SerpentCipher;
import com.rootnetsec.cryptofile.cipher.Cipher;

public class Serpent extends Cipher {
    public Serpent() {
        super();
    }
    @Override
    public void decryptFile(String srcFile, String destFile, String userKey) {
        SerpentCipher.decryptFile(srcFile, destFile, userKey);
    }
    @Override
    public void encryptFile(String srcFile, String destFile, String userKey) {
        SerpentCipher.encryptFile(srcFile, destFile, userKey);
    }


}
