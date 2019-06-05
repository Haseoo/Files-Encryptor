package com.rootnetsec.cryptofile.cipher.javaCipher;

/**
 * The class that uses AES/CBC/PKCS5PADDING as encryption algorithm
 */
public final class AESCipher extends JavaCipher {
    public AESCipher() {
        super("AES", "AES/CBC/PKCS5PADDING", "BC", EncryptionType.AES);

    }
}