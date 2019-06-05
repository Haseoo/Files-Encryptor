package com.rootnetsec.cryptofile.cipher.filemanager;

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

    static public final long MAX_CHUNK_SIZE = Runtime.getRuntime().freeMemory();
    static public final int SHORT_HEADER_SIZE = Short.BYTES + Byte.BYTES + Integer.BYTES;
    static  public final short MAGIC_HEADER = (short)0xDEAD;



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


}