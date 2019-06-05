package com.rootnetsec.cryptofile.cipher.filemanager;

import java.io.*;

/**
 * Abstract class used to manage files that are encrypted or for encryption
 */
abstract public class FileManager implements AutoCloseable {
    /**Input file stream*/
    protected InputStream inputStream;
    /**Output file stream*/
    protected OutputStream outputStream;
    /**The size of input file*/
    protected long fileSize;

    /**The size of the current file chunk*/
    protected int chunkSize;
    /**The index of the current file chunk*/
    protected int currentChunk;
    /**The total number of chunks in the file*/
    protected int numberOfChunks;

    /**The maximum size of a file chunk depends on current amount free RAM of JVM*/
    static public final long MAX_CHUNK_SIZE = Runtime.getRuntime().freeMemory();
    /**The magic number- first two bytes of the file header*/
    static  public final short MAGIC_HEADER = (short)0xDEAD;
    /**The size of 7 bytes long short header contains 2 bytes long magic number,
     *  one byte that specifies encryption algorithm and the size of long header*/
    static public final int SHORT_HEADER_SIZE = Short.BYTES + Byte.BYTES + Integer.BYTES;


    /** Open files with given paths
     * @param inputPath The path of an input file
     * @param outputPath The path of an output file
     * @throws FileNotFoundException Thrown when given files don't exist
     */
    public FileManager(String inputPath, String outputPath) throws FileNotFoundException {
        inputStream = new FileInputStream(inputPath);
        outputStream = new FileOutputStream(outputPath);
        fileSize = new File(inputPath).length();
        currentChunk = 0;
    }

    /**Implements the autoCloseable interface, closes files streams
     * @throws IOException Thrown when an IO error occurred
     */
    @Override
    final public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }


    /** Returns next chunk of an input file
     * @return Next chunk of an input file
     * @throws IOException Thrown when the file can not be accessed
     */
    abstract public byte[] getChunk() throws IOException;

    /** Writes a chunk of data in an output file
     * @param data The chunk of data
     * @throws IOException Thrown when the file can not be accessed or written
     */
    abstract public void writeChunk(byte[] data) throws IOException;

    /** Returns the number of total chunks of an input file
     * @return Number of total chunks of an input file
     */
    public int getNumberOfChunks() {
        return this.numberOfChunks;
    }


}