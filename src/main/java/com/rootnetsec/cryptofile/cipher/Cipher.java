package com.rootnetsec.cryptofile.cipher;

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.ByteBuffer;

import com.rootnetsec.cryptofile.cipher.filemanager.FileManager;
import com.rootnetsec.cryptofile.cipher.javaCipher.*;

import java.security.GeneralSecurityException;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

/**
 * The abstract class that is used to files encryption
 */
abstract public class Cipher {

    /**
     * Enumeration type that is used to distinguish encryption algorithm
     */
    public enum EncryptionType {
        /**
         * Value for AES algorithm
         */
        AES,
        /**
         * Value for TwoFish algorithm
         */
        TWOFISH,
        /**
         * Value for serpent algorithm
         */
        SERPENT
    }

    /** Static map that is used to transform string argument into EncryptionType enumeration*/
    static public final Map<String, EncryptionType> TYPE_PARSE_MAP = ImmutableMap.of(
            "AES", EncryptionType.AES,
            "TwoFish",EncryptionType.TWOFISH,
            "Serpent", EncryptionType.SERPENT
    );

    /**
     * The number of input file chunks
     */
    protected volatile int numberOfChunks;
    /**
     * Boolean value used to inform that encrypting or decrypting is done
     */
    private boolean workDone = false;
    /**
     * The index of the last encrypted/decrypted chunk
     */
    private int currentChunk;


    /** Return a Cipher class instance based on given enum value
     * @param type Enum value that indicates encryption algorithm
     * @return a Cipher class instance
     */
    public static Cipher getInstance(EncryptionType type) {
        Cipher retVal = null;
        switch (type) {
            case AES:
                retVal = new AESCipher();
                break;
            case TWOFISH:
                retVal = new TwofishCipher();
                break;
            case SERPENT:
                retVal = new SerpentCipher();
                break;
        }
        return retVal;
    }

    /** eturn a Cipher class instance based on header of an encrypted file
     * @param srcFile Encrypted file path
     * @return a Cipher class instance
     * @throws IOException Thrown when the file has invalid header or other IO error occurred
     */
    public static Cipher getInstance(String srcFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(srcFile);
        byte[] header = inputStream.readNBytes(FileManager.SHORT_HEADER_SIZE);
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        short magicHeaderRead = headerBuffer.getShort();
        if (magicHeaderRead != FileManager.MAGIC_HEADER) {
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
                retVal = new TwofishCipher();
                break;
            case 3:
                return new SerpentCipher();
            default:
                inputStream.close();
                throw new InvalidHeaderException("Encrypted file has invalid header");
        }

        inputStream.close();
        return retVal;
    }

    /** Returns the number of input file chunks
     * @return The number of input file chunks
     */
    public int getNumberOfChunks() {
        return numberOfChunks;
    }

    /** Returns the index of the last encrypted/decrypted chunk in synchronized way
     * @return The index of the last encrypted/decrypted chunk
     */
    public synchronized int getCurrentChunk() {
        return currentChunk;
    }

    /** Allows to set currentChunk flied in synchronized way
     * @param currentChunk value to set
     */
    protected synchronized void setCurrentChunk(int currentChunk) {
        this.currentChunk = currentChunk;
    }

    /** Tells if the encryption or decryption process was finished in synchronized way
     * @return workDone filed value
     */
    synchronized  public boolean isWorkDone() {
        return workDone;
    }

    /**
     * Allows to set workDone filed to true in synchronized way
     */
    synchronized  protected void setWorkDone() {
        this.workDone = true;
    }

    /** Abstract method that decrypts a file
     * @param srcFile input file path
     * @param destFile output file path
     * @param userKey user password
     * @throws GeneralSecurityException Thrown when an decryption error occurred
     */
    public abstract void decryptFile(String srcFile, String destFile, String userKey) throws GeneralSecurityException;

    /** Abstract method that encrypts a file
     * @param srcFile input file path
     * @param destFile output file path
     * @param userKey user password
     * @throws GeneralSecurityException Thrown when an encryption error occurred
     */
    public abstract void encryptFile(String srcFile, String destFile, String userKey) throws GeneralSecurityException;
}