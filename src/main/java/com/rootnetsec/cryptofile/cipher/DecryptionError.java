package com.rootnetsec.cryptofile.cipher;

import java.security.GeneralSecurityException;

/**
 * The exception that is thrown when an decryption error occurred
 */
public class DecryptionError extends GeneralSecurityException {
    public DecryptionError(String message) {
        super("Decryption error: " + message);
    }
}
