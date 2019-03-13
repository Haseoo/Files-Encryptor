package com.rootnetsec.cryptofile;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;

import java.security.GeneralSecurityException;

public class Crypto {
    Aead aead;

    public Crypto() {
        try {
            AeadConfig.register();
            KeysetHandle keysetHandle = KeysetHandle.generateNew(
                AeadKeyTemplates.AES128_GCM);
        
            aead = AeadFactory.getPrimitive(keysetHandle);

        } catch (GeneralSecurityException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public byte[] encrypt(byte[] plaintext, byte[] key) {
        try {
            byte[] ciphertext = aead.encrypt(plaintext, key);
            return ciphertext;

        } catch (GeneralSecurityException e) {
            System.out.println(e);
            System.exit(1);
        }
        return null;
    }

    public byte[] decrypt(byte[] ciphertext, byte[] key) {
        try {
            byte[] plaintext = aead.decrypt(ciphertext, key);
            return plaintext;

        } catch (GeneralSecurityException e) {
            System.out.println(e);
            System.exit(1);
        }
        return null;
    }


}