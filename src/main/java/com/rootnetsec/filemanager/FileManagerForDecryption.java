package com.rootnetsec.filemanager;

import java.io.*;
import java.util.Arrays;
import java.nio.ByteBuffer;
import com.rootnetsec.cryptofile.PBKDF2Hashing;

public class FileManagerForDecryption extends FileManager {
    public FileManagerForDecryption(String inputPath, String outputPath) throws FileNotFoundException, IOException, FileManagerHeaderInvalidException {
        super(inputPath, outputPath);
        byte[] header = new byte[headerSize];
        inputStream.read(header, 0, headerSize);
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        
        short tmpMagicHeader = headerBuffer.getShort();

        if (tmpMagicHeader != magicHeader) {
            throw new FileManagerHeaderInvalidException("Magic header invalid");
        }

        numberOfChunks = headerBuffer.getInt();
        int tmpSaltBytesLength = headerBuffer.getInt();

        if (tmpSaltBytesLength != PBKDF2Hashing.SALT_BYTES) {
            throw new FileManagerHeaderInvalidException("Salt bytes length invalid");
        }

        byte[] salt = new byte[PBKDF2Hashing.SALT_BYTES];
        headerBuffer.get(salt);

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
        outputStream.write(data.length);
        outputStream.write(data);
    }


}