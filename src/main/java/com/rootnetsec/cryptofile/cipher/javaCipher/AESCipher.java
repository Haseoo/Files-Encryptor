package com.rootnetsec.cryptofile.cipher.javaCipher;

public final class AESCipher extends JavaCipher {
    public AESCipher() {
        super("AES", "AES/CBC/PKCS5PADDING", "BC", EncryptionType.AES);

    }
}