package com.rootnetsec.filemanager;

import java.io.*;

abstract public class FileManager {
    protected String    inPath, 
                        outPath;

    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected long fileSize;

    protected int   chunkSize, 
                    currentChunk, 
                    numberOfChunks;

    static final long maxChunkSize = 0x10;//Integer.MAX_VALUE / 3;

    static final int headerSize = 54;
    static final short magicHeader = (short)0xDEAD;

    byte[] salt, iv;

    public FileManager(String inputPath, String outputPath) throws FileNotFoundException {
        inputStream = new FileInputStream(inputPath);
        outputStream = new FileOutputStream(outputPath);
        fileSize = new File(inputPath).length();
        currentChunk = 0;
    }

    abstract public byte[] getChunk() throws IOException, IndexOutOfBoundsException;
    abstract public void writeChunk(byte[] data) throws IOException;

    public int getNumberOfChunks() {
        return this.numberOfChunks;
    }

    public int getNumberOfCurrentChunk() {
        return this.currentChunk;
    }


}