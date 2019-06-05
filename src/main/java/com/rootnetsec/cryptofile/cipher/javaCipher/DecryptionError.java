package com.rootnetsec.cryptofile.cipher.javaCipher;

import java.security.GeneralSecurityException;

public class DecryptionError extends GeneralSecurityException {
    public DecryptionError(String message) {
        super("Decryption error: " + message);
    }
}
