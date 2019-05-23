package com.rootnetsec.filemanagern;

import java.io.*;
import java.nio.ByteBuffer;

public class FileManagerForDecryption extends FileManager {
    private boolean headerReaded = false;

    public FileManagerForDecryption(String inputPath, String outputPath) throws FileNotFoundException, IOException, FileManagerHeaderInvalidException {
        super(inputPath, outputPath);
        byte[] header = new byte[shortHeaderSize];
        inputStream.read(header, 0, shortHeaderSize);
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        
        short tmpMagicHeader = headerBuffer.getShort();
        if (tmpMagicHeader != magicHeader) {
            throw new FileManagerHeaderInvalidException("Magic header invalid");
        }

        headerBuffer.get(); //Skips type flag

        numberOfChunks = headerBuffer.getInt();

    }

    public byte[] getChunk() throws IOException {
        if (currentChunk > numberOfChunks) {
            throw new IndexOutOfBoundsException("Index " + currentChunk + " is out of bounds!");
        }
        if (!headerReaded) {
            //TODO throw runtime error
        }
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

    public byte[] getHeader(int headerSize) throws IOException {
        byte[] header = null;
        if (!headerReaded) {
            inputStream.reset();
            header = new byte [headerSize];
            inputStream.read(header);
            headerReaded = true;
        } else {
           //TODO throw runtime error
        }
        return header;
    }
    

}