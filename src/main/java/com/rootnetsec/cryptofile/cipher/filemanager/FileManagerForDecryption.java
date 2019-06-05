package com.rootnetsec.cryptofile.cipher.filemanager;

import java.io.*;
import java.nio.ByteBuffer;

import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.cipher.javaCipher.JavaCipher;


public class FileManagerForDecryption extends FileManager {
    private byte[] salt,
                   iv;

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


    public void writeChunk(byte[] data) throws IOException {
        outputStream.write(data);
    }

    public byte[] getIV() {
        return this.iv;
    }

    public byte[] getSalt() {
        return this.salt;
    }

}