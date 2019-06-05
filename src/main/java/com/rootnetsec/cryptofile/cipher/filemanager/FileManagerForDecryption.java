package com.rootnetsec.cryptofile.cipher.filemanager;

import java.io.*;
import java.nio.ByteBuffer;

import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.cipher.javaCipher.JavaCipher;


/**
 * The class that is used to read encrypted files
 */
public class FileManagerForDecryption extends FileManager {
    /**Salt read from file*/
    private byte[] salt;
    /**IV read form file*/
    private byte[] iv;

    /** Opens files and reads reads the header of an encrypted file
     * @param inputPath The path of an input file
     * @param outputPath The path of an output file
     * @exception IOException Thrown when the file has invalid header or other IO error occurred
    */
    public FileManagerForDecryption(String inputPath, String outputPath) throws IOException {
        super(inputPath, outputPath);

        final int LONG_HEADER_SIZE;

        byte[] shortHeader = new byte[SHORT_HEADER_SIZE];
        inputStream.read(shortHeader, 0, SHORT_HEADER_SIZE);
        ByteBuffer shortHeaderBuffer = ByteBuffer.wrap(shortHeader);
        
        short tmpMagicHeader = shortHeaderBuffer.getShort();
        if (tmpMagicHeader != MAGIC_HEADER) {
            inputStream.close();
            outputStream.close();
            throw new FileManagerHeaderInvalidException("Magic header invalid");
        }

        shortHeaderBuffer.position(shortHeaderBuffer.position() + Byte.BYTES);

        LONG_HEADER_SIZE = shortHeaderBuffer.getInt();
        byte[] longHeader = new byte[LONG_HEADER_SIZE];

        inputStream.read(longHeader, 0, LONG_HEADER_SIZE);

        ByteBuffer headerBuffer = ByteBuffer.wrap(longHeader);

        numberOfChunks = headerBuffer.getInt();

        int tmpSaltSize = headerBuffer.getInt();
        if (tmpSaltSize != PBKDF2Hashing.SALT_BYTES) {
            inputStream.close();
            outputStream.close();
            throw new FileManagerHeaderInvalidException("Salt bytes length invalid");
        }
        salt = new byte[PBKDF2Hashing.SALT_BYTES];
        headerBuffer.get(salt);

        int tmpIVSize = headerBuffer.getInt();
        if (tmpIVSize != JavaCipher.IV_LENGTH) {
            inputStream.close();
            outputStream.close();
            throw new FileManagerHeaderInvalidException("IV bytes length invalid");
        }
        iv = new byte[JavaCipher.IV_LENGTH];
        headerBuffer.get(iv);

    }

    /**
     * @return Returns next chunk of the encrypted file
     * @throws IOException Thrown when the file can not be accessed
     */

    public byte[] getChunk() throws IOException {
        if (currentChunk >= numberOfChunks) {
            throw new IndexOutOfBoundsException("Index " + currentChunk + " is out of bounds!");
        }
        System.gc();
        byte[] chunkSizeByte = new byte[4];
        inputStream.read(chunkSizeByte);
        ByteBuffer chunkSizeByteBuffer = ByteBuffer.wrap(chunkSizeByte);
        chunkSize = chunkSizeByteBuffer.getInt();
        byte[] chunk = new byte[chunkSize];
        inputStream.read(chunk, 0, chunkSize);
        currentChunk++;
        return chunk;
    }


    /** Writes a chunk of data in the output file
     * @param data The chunk of data
     * @throws IOException Thrown when the file can not be accessed or written
     */
    public void writeChunk(byte[] data) throws IOException {
        outputStream.write(data);
    }

    /** Returns IV that was read from file
     * @return IV that was read from file
     */
    public byte[] getIV() {
        return this.iv;
    }

    /** Returns salt that was read from file
     * @return Salt that was read from file
     */
    public byte[] getSalt() {
        return this.salt;
    }

}