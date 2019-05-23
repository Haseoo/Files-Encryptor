package com.rootnetsec.cryptofile.cipher;

abstract class Cipher {
    protected final long chunkSize;
    
    private Cipher(long chunkSize) {
        super();
        this.chunkSize = chunkSize;
    }
    public enum CipherType {one, two, three};

    public static Cipher getInstance(CipherType cipherType, long chunkSize) {
        return null;
    }

    public static Cipher getInstance(FileManager file) { //TU będzie fileManagerForDecryption in di fjuczer
        return null;
    }

    public abstract void decryptFile(String srcFile, String destFile, String userKey);
    public abstract void encryptFile(String srcFile, String destFile, String userKey); //TU będzie fileManagerForDecryption in di fjuczer
}