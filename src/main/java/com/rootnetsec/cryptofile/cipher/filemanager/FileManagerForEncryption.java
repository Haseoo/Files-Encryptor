package com.rootnetsec.cryptofile.cipher.filemanager;

import com.rootnetsec.cryptofile.cipher.Cipher;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;

public class FileManagerForEncryption extends FileManager {
    public FileManagerForEncryption(String inputPath, String outputPath, byte[] salt, byte[] iv, Cipher.EncryptionType type) throws Exception{
        super(inputPath, outputPath);
        numberOfChunks = (int)(Math.ceil((fileSize / (double) MAX_CHUNK_SIZE)));
        outputStream.write(prepareHeader(salt, iv, type));
    }

    private byte[] prepareHeader(byte[] salt, byte[] iv, Cipher.EncryptionType type) throws Exception {
        final int HEADER_SIZE;
        final int LONG_HEADER_SIZE;
        final int LONG_ENCRYPTED_HEADER_SIZE;

        LONG_HEADER_SIZE = 3 * Integer.BYTES + salt.length + iv.length;
        ByteBuffer longHeader = ByteBuffer.allocate(LONG_HEADER_SIZE);

        longHeader.putInt(numberOfChunks);
        longHeader.putInt(salt.length);
        longHeader.put(salt);
        longHeader.putInt(iv.length);
        longHeader.put(iv);

        byte[] encryptedLongHeader = encryptHeader(longHeader.array());
        LONG_ENCRYPTED_HEADER_SIZE = encryptedLongHeader.length;

        HEADER_SIZE = Short.BYTES + Byte.BYTES + Integer.BYTES + LONG_ENCRYPTED_HEADER_SIZE;

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

        header.putInt(LONG_ENCRYPTED_HEADER_SIZE);
        header.put(encryptedLongHeader);

        return header.array();
    }

    public byte[] getChunk() throws IOException {
        if (currentChunk > numberOfChunks) {
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

    private byte[] encryptHeader(byte[] header) throws Exception {


        final SecretKeySpec key = new SecretKeySpec(HASH, "AES");
        IvParameterSpec parameters = new IvParameterSpec(IV);
        final javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, parameters);
        byte[] tmp = cipher.doFinal(header);

        return tmp;

        }



}