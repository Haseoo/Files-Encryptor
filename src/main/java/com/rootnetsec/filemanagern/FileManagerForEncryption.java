package com.rootnetsec.filemanagern;

import java.io.*;
import java.nio.ByteBuffer;

public class FileManagerForEncryption extends FileManager {
    private boolean headerSet = false;
    
    public FileManagerForEncryption(String inputPath, String outputPath, byte[] salt, byte[] iv) throws FileNotFoundException , IOException{
        super(inputPath, outputPath);
        numberOfChunks = (int)(Math.ceil((fileSize / (double)maxChunkSize)));
    }

    public void setHeader(byte[] header) throws IOException {
        if (!headerSet) {
            //TODO thow runtime exception
        }
        outputStream.write(header);
        headerSet = true;

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
        if (!headerSet) {
            //TODO thow runtime exception
        }
        ByteBuffer chunkSizeByte = ByteBuffer.allocate(Integer.BYTES);
        chunkSizeByte.putInt(data.length);
        outputStream.write(chunkSizeByte.array());
        outputStream.write(data);
    }

    


}