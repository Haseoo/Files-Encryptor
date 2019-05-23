package com.rootnetsec.cryptofile.cipher.aes;

import com.rootnetsec.cryptofile.AesCipher;
import com.rootnetsec.cryptofile.cipher.Cipher;

public class AES extends Cipher {
    public AES() {
        super();
    }
    @Override
    public void decryptFile(String srcFile, String destFile, String userKey) {
        AesCipher.decryptFile(srcFile, destFile, userKey);
    }
    @Override
    public void encryptFile(String srcFile, String destFile, String userKey) {
         AesCipher.encryptFile(srcFile, destFile, userKey);
    }


}
