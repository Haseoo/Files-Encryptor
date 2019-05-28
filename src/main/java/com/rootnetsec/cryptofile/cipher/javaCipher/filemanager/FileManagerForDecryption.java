package com.rootnetsec.cryptofile.cipher.javaCipher.filemanager;

import java.io.*;
import java.nio.ByteBuffer;
import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.cipher.javaCipher.JavaSTDCipher;

public class FileManagerForDecryption extends FileManager {
    private byte[] salt,
                   iv;

    public FileManagerForDecryption(String inputPath, String outputPath) throws FileNotFoundException, IOException, FileManagerHeaderInvalidException {
        super(inputPath, outputPath);
        byte[] header = new byte[headerSize];
        inputStream.read(header, 0, headerSize);
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        
        short tmpMagicHeader = headerBuffer.getShort();
        if (tmpMagicHeader != magicHeader) {
            throw new FileManagerHeaderInvalidException("Magic header invalid");
        }

        headerBuffer.get(); //skips byte about type

        numberOfChunks = headerBuffer.getInt();

        int tmpSaltSize = headerBuffer.getInt();
        if (tmpSaltSize != PBKDF2Hashing.SALT_BYTES) {
            throw new FileManagerHeaderInvalidException("Salt bytes lenght invalid");
        }
        salt = new byte[PBKDF2Hashing.SALT_BYTES];
        headerBuffer.get(salt);

        int tmpIVSize = headerBuffer.getInt();
        if (tmpIVSize != JavaSTDCipher.IV_LENGTH) {
            throw new FileManagerHeaderInvalidException("IV bytes lenght invalid");
        }
        iv = new byte[JavaSTDCipher.IV_LENGTH];
        headerBuffer.get(iv);

    }

    public byte[] getChunk() throws IOException {
        if (currentChunk > numberOfChunks) {
            throw new IndexOutOfBoundsException("Index " + currentChunk + " is out of bounds!");
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

    public byte[] getIV() {
        return this.iv;
    }

    public byte[] getSalt() {
        return this.salt;
    }

}