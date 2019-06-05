package com.rootnetsec.cryptofile.cipher.filemanager;

import com.rootnetsec.cryptofile.cipher.Cipher;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

public class FileManagerForEncryption extends FileManager {
    public FileManagerForEncryption(String inputPath, String outputPath,
                                    byte[] salt, byte[] iv,
                                    Cipher.EncryptionType type) throws IOException {
        super(inputPath, outputPath);
        numberOfChunks = (int)(Math.ceil((fileSize / (double) MAX_CHUNK_SIZE)));
        outputStream.write(prepareHeader(salt, iv, type));
    }

    private byte[] prepareHeader(byte[] salt, byte[] iv, Cipher.EncryptionType type) {
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

    public void writeChunk(byte[] data) throws IOException {
        ByteBuffer chunkSizeByte = ByteBuffer.allocate(Integer.BYTES);
        chunkSizeByte.putInt(data.length);
        outputStream.write(chunkSizeByte.array());
        outputStream.write(data);
    }

}