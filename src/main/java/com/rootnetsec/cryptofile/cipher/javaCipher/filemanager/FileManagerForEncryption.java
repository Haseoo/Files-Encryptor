package com.rootnetsec.cryptofile.cipher.javaCipher.filemanager;

import java.io.*;
import java.nio.ByteBuffer;

public class FileManagerForEncryption extends FileManager {
    public FileManagerForEncryption(String inputPath, String outputPath, byte[] salt, byte[] iv) throws FileNotFoundException , IOException{
        super(inputPath, outputPath);
        numberOfChunks = (int)(Math.ceil((fileSize / (double)maxChunkSize)));
        outputStream.write(prepareHeader(salt, iv));
    }

    private byte[] prepareHeader(byte[] salt, byte[] iv) {
        ByteBuffer header = ByteBuffer.allocate(headerSize);
        header.putShort(magicHeader);

        header.put((byte)1);

        header.putInt(numberOfChunks);

        header.putInt(salt.length);

        header.put(salt);

        header.putInt(iv.length);

        header.put(iv);
        
        return header.array();
    }

    public byte[] getChunk() throws IOException {
        if (currentChunk > numberOfChunks) {
            throw new IndexOutOfBoundsException("Index " + currentChunk + " is out of bounds!");
        }

        long restOfFile = fileSize - chunkSize * currentChunk;
        chunkSize = (int)((restOfFile < maxChunkSize) ? restOfFile : maxChunkSize);
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