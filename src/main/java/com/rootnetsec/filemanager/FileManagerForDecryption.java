package com.rootnetsec.filemanager;

import java.io.*;
import java.util.Arrays;
import java.nio.ByteBuffer;
import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.AesCipher;

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

        numberOfChunks = headerBuffer.getInt();

        int tmpSaltSize = headerBuffer.getInt();
        if (tmpSaltSize != PBKDF2Hashing.SALT_BYTES) {
            throw new FileManagerHeaderInvalidException("Salt bytes lenght invalid");
        }
        salt = new byte[PBKDF2Hashing.SALT_BYTES];
        headerBuffer.get(salt);

        int tmpIVSize = headerBuffer.getInt();
        if (tmpIVSize != AesCipher.IV_LENGHT) {
            throw new FileManagerHeaderInvalidException("IV bytes lenght invalid");
        }
        iv = new byte[AesCipher.IV_LENGHT];
        AesCipher.IV_LENGHT.get(iv);

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
        ByteBuffer chunkSizeByte = ByteBuffer.allocate(Integer.BYTES);
        chunkSizeByte.putInt(data.length);
        outputStream.write(chunkSizeByte.array());
        outputStream.write(data);
    }


}