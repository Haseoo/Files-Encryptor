package com.rootnetsec.cryptofile.cipher.javaCipher;

import java.security.GeneralSecurityException;

public class EncryptionError extends GeneralSecurityException {
    public EncryptionError(String message) {
        super("Encryption error: " + message);
    }
}
