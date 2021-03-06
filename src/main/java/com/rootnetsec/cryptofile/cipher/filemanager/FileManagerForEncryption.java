package com.rootnetsec.cryptofile.cipher.filemanager;

import com.rootnetsec.cryptofile.cipher.Cipher;

import java.io.*;
import java.nio.ByteBuffer;


/**
 * Class that is used to manage files that are going to be encrypted
 */
public class FileManagerForEncryption extends FileManager {
    /**The salt that is used in the encryption process*/
    private byte[] salt;
    /**The IV that is used in the encryption process*/
    private byte[] iv;
    /**Used encryption algorithm*/
    private Cipher.EncryptionType type;

    /** Opens files, initializes numberOfChunks and prepares output file header
     * @param inputPath The path of an input file
     * @param outputPath The path of an output file
     * @param salt The salt that is used in the encryption process
     * @param iv The IV that is used in the encryption process
     * @param type Used encryption algorithm
     * @throws IOException Thrown when an IO error occurred
     */
    public FileManagerForEncryption(String inputPath, String outputPath,
                                    byte[] salt, byte[] iv,
                                    Cipher.EncryptionType type) throws IOException {
        super(inputPath, outputPath);
        this.salt = salt;
        this.iv = iv;
        this.type = type;
        numberOfChunks = (int)(Math.ceil((fileSize / (double) MAX_CHUNK_SIZE)));
        outputStream.write(prepareHeader());
    }

    /** Prepares end returns file header
     * @return prepared file header
     */
    private byte[] prepareHeader() {
        final int HEADER_SIZE;
        final int LONG_HEADER_SIZE;

        LONG_HEADER_SIZE = 3 * Integer.BYTES + salt.length + iv.length;
        HEADER_SIZE = LONG_HEADER_SIZE + SHORT_HEADER_SIZE;
        ByteBuffer header = ByteBuffer.allocate(HEADER_SIZE);

        header.putShort(MAGIC_HEADER);

        byte encType = 0;
        switch (type) {
            case AES:
                encType = 1;
                break;
            case TWOFISH:
                encType = 2;
                break;
            case SERPENT:
                encType = 3;
                break;
        }
        header.put(encType);
        header.putInt(LONG_HEADER_SIZE);

        header.putInt(numberOfChunks);
        header.putInt(salt.length);
        header.put(salt);
        header.putInt(iv.length);
        header.put(iv);

        return header.array();
    }

    /**
     * @return Returns next chunk of the input file
     * @throws IOException Thrown when the file can not be accessed
     */
    public byte[] getChunk() throws IOException {
        if (currentChunk >= numberOfChunks) {
            throw new IndexOutOfBoundsException("Index " + currentChunk + " is out of bounds!");
        }
        System.gc();

        long restOfFile = fileSize - chunkSize * currentChunk;
        chunkSize = (int)((restOfFile < MAX_CHUNK_SIZE) ? restOfFile : MAX_CHUNK_SIZE);
        byte[] chunk = new byte[chunkSize];
        inputStream.read(chunk, 0, chunkSize);
        currentChunk++;
        return chunk;
    }

    /** Writes a chunk of data in the encrypted file
     * @param data The chunk of data
     * @throws IOException Thrown when the file can not be accessed or written
     */
    public void writeChunk(byte[] data) throws IOException {
        ByteBuffer chunkSizeByte = ByteBuffer.allocate(Integer.BYTES);
        chunkSizeByte.putInt(data.length);
        outputStream.write(chunkSizeByte.array());
        outputStream.write(data);
    }

}