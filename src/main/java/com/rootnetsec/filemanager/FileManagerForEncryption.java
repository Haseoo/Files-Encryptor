package com.rootnetsec.filemanager;

import java.io.*;

public class FileManagerForEncryption extends FileManager {
    public FileManagerForEncryption(String inputPath, String outputPath) throws FileNotFoundException {
        super(inputPath, outputPath);
        numberOfChunks = (int)(Math.ceil((fileSize / (double)maxChunkSize)));
        outputStream = new FileOutputStream(outputPath);
    }

    public byte[] getChunk() throws IOException, IndexOutOfBoundsException {
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
        if (currentChunk == 0) {
            System.out.println("Add header.");
        }

        outputStream.write(data.length);
        outputStream.write(data);
    }

    


}