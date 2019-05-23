package com.rootnetsec.cryptofile.cipher.AES;

import com.rootnetsec.cryptofile.cipher;
import com.rootnetsec.cryptofile.AesCipher;

class AES extends Cipher {
    void decryptFile(String srcFile, String destFile, String userKey) {
        AesCipher.decryptFile(srcFile, destFile, userKey);
    }
    void encryptFile(String srcFile, String destFile, String userKey) {
         AesCipher.encryptFile(srcFile, destFile, userKey);
    }


}
