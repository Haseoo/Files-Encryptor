package com.rootnetsec.cryptofile.cipher;

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import com.rootnetsec.cryptofile.cipher.javaCipher.*;

abstract public class Cipher {
    enum EncryptionType {
        FOO
    }
    static public final short magicHeader = (short)0xDEAD;

    private volatile int numberOFChunks;
                
    private int currentChunk;    
    protected Cipher() {
        super();
    }
    public enum CipherType {one, two, three};

    public static Cipher getInstance(CipherType cipherType, long chunkSize) {
        return null;
    }

    public static Cipher getInstance(String srcFile) throws IOException, InvalidHeaderException {
        FileInputStream inputStream = new FileInputStream(srcFile);
        byte[] header = inputStream.readNBytes(3);
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        short magicHeaderRead = headerBuffer.getShort();
        if (magicHeaderRead != magicHeader) {
            inputStream.close();
            throw new InvalidHeaderException("Encypted file has invalid header");
        }
        byte type = headerBuffer.get();
        Cipher retVal = null;
        if (type == 1) {
            retVal = new AESCipher();
        } else {
            inputStream.close();
            throw new InvalidHeaderException("Encypted file has invalid header");
        }

        inputStream.close();
        return retVal;
    }

    public static Cipher getInstance(EncryptionType type) {
        return null;
    }

    public int getNumberOfChunks() {
        return numberOFChunks;
    }

    public synchronized int getCurrentChunk() {
        return currentChunk;
    }

    protected synchronized void setCurrentChunk(int currentChunk) {
        this.currentChunk = currentChunk;
    }

    public abstract void decryptFile(String srcFile, String destFile, String userKey) throws Exception;
    public abstract void encryptFile(String srcFile, String destFile, String userKey) throws Exception; 
}