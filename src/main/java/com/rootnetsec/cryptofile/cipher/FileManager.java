package com.rootnetsec.cryptofile.cipher;

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

    final long maxChunkSize;
    
    static final short magicHeader = (short)0xDEAD;


    public FileManager(String inputPath, String outputPath, long maxChunkSize) throws FileNotFoundException {
        inputStream = new FileInputStream(inputPath);
        outputStream = new FileOutputStream(outputPath);
        fileSize = new File(inputPath).length();
        this.maxChunkSize = maxChunkSize;
        currentChunk = 0;
    }

    @Override
    final public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    abstract public byte[] getChunk() throws IOException;
    abstract public void writeChunk(byte[] data) throws IOException;

    public void writeHeader(byte[] header) {
        
    }
    public byte[] getHeader() {
        return null;
    }

    public int getNumberOfChunks() {
        return this.numberOfChunks;
    }

    public int getNumberOfCurrentChunk() {
        return this.currentChunk;
    }


}