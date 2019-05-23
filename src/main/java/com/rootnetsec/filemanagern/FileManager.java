package com.rootnetsec.filemanagern;

import com.rootnetsec.cryptofile.cipher.Cipher;

import java.io.*;

abstract public class FileManager implements AutoCloseable {
    protected String    inPath, 
                        outPath;

    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected long fileSize;

    protected int   chunkSize, 
                    currentChunk, 
                    numberOfChunks;

    
    static public final short magicHeader = Cipher.magicHeader;
    static public final int shortHeaderSize = 7;
    static public final int maxChunkSize = Integer.MAX_VALUE / 3;


    public FileManager(String inputPath, String outputPath) throws FileNotFoundException {
        inputStream = new FileInputStream(inputPath);
        outputStream = new FileOutputStream(outputPath);
        fileSize = new File(inputPath).length();
        currentChunk = 0;
    }

    @Override
    final public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    abstract public byte[] getChunk() throws IOException;
    abstract public void writeChunk(byte[] data) throws IOException;

    public int getNumberOfChunks() {
        return this.numberOfChunks;
    }

    public int getNumberOfCurrentChunk() {
        return this.currentChunk;
    }


}