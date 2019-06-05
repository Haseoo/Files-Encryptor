package com.rootnetsec.cryptofile.cipher;

import java.security.GeneralSecurityException;

/**
 * The exception that is thrown when an decryption error occurred
 */
public class EncryptionError extends GeneralSecurityException {
    public EncryptionError(String message) {
        super("Encryption error: " + message);
    }
}
