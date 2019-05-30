package com.rootnetsec.cryptofile.cipher;

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import com.rootnetsec.cryptofile.cipher.javaCipher.*;

import java.util.Map;
import com.google.common.collect.ImmutableMap;

abstract public class Cipher {


    public enum EncryptionType {
        AES,
        TWOFISH,
        SERPENT
    }

    static public final Map<String, EncryptionType> TYPE_PARSE_MAP = ImmutableMap.of(
            "AES", EncryptionType.AES,
            "TwoFish",EncryptionType.TWOFISH,
            "Serpent", EncryptionType.SERPENT
    );

    static public final short magicHeader = (short)0xDEAD;

    protected volatile int numberOfChunks;
    private boolean workDone = false;
                
    private int currentChunk;    
    protected Cipher() {
        super();
    }

    public static Cipher getInstance(EncryptionType type) {
        Cipher retVal = null;
        switch (type) {
            case AES:
                retVal = new AESCipher();
                break;
            case TWOFISH:
                retVal = new Twofish();
                break;
            case SERPENT:
                retVal = new Serpent();
                break;
        }
        return retVal;
    }

    public static Cipher getInstance(String srcFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(srcFile);
        byte[] header = inputStream.readNBytes(3);
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        short magicHeaderRead = headerBuffer.getShort();
        if (magicHeaderRead != magicHeader) {
            inputStream.close();
            throw new InvalidHeaderException("Encrypted file has invalid header");
        }
        byte type = headerBuffer.get();
        Cipher retVal;
        switch (type) {
            case 1:
                retVal = new AESCipher();
                break;
            case 2:
                retVal = new Twofish();
                break;
            case 3:
                return new Serpent();
            default:
                inputStream.close();
                throw new InvalidHeaderException("Encrypted file has invalid header");
        }

        inputStream.close();
        return retVal;
    }

    public int getNumberOfChunks() {
        return numberOfChunks;
    }

    public synchronized int getCurrentChunk() {
        return currentChunk;
    }

    protected synchronized void setCurrentChunk(int currentChunk) {
        this.currentChunk = currentChunk;
    }

    synchronized  public boolean isWorkDone() {
        return workDone;
    }

    synchronized  protected void setWorkDone() {
        this.workDone = true;
    }

    public abstract void decryptFile(String srcFile, String destFile, String userKey) throws Exception;
    public abstract void encryptFile(String srcFile, String destFile, String userKey) throws Exception; 
}