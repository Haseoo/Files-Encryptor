package com.rootnetsec.cryptofile.cipher.filemanager;

import com.rootnetsec.cryptofile.cipher.Cipher;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileManagerForEncryptionTest {

    @Test
    public void getNumberOfChunks() throws Exception{
        String inFilePath  = "src/test/resources/TestFile.txt";
        String outFilePath = "src/test/resources/dummy.enc";
        try(FileManagerForEncryption fileManagerForEncryption =
                    new FileManagerForEncryption(inFilePath, outFilePath, new byte[] {1},new byte[] {1}, Cipher.EncryptionType.AES)) {

            long numberOfModelFileChunks = (long) Math.ceil(new File(inFilePath).length() / (double) FileManager.MAX_CHUNK_SIZE);
            assertEquals(numberOfModelFileChunks, fileManagerForEncryption.getNumberOfChunks());
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void getChunk() throws Exception{
        String inFilePath  = "src/test/resources/TestFile.txt";
        String outFilePath = "src/test/resources/dummy.enc";
        byte[] model = {84, 101, 115, 116, 115, 32, 97, 114, 101, 32, 118, 101, 114, 121, 32, 105, 109, 112, 111, 114, 116, 97, 110, 116, 33};
        try(FileManagerForEncryption fileManagerForEncryption =
                    new FileManagerForEncryption(inFilePath, outFilePath, new byte[] {1},new byte[] {1}, Cipher.EncryptionType.AES)) {

            assertTrue(Arrays.equals(fileManagerForEncryption.getChunk(), model));

        } catch (Exception e) {
            throw e;
        }
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getChunkException() throws Throwable{
        String inFilePath  = "src/test/resources/TestFile.txt";
        String outFilePath = "src/test/resources/dummy.enc";
        try(FileManagerForEncryption fileManagerForEncryption =
                    new FileManagerForEncryption(inFilePath, outFilePath, new byte[] {1},new byte[] {1}, Cipher.EncryptionType.AES)) {

           fileManagerForEncryption.getChunk();
           fileManagerForEncryption.getChunk();

        } catch (Throwable e) {
            throw e;
        }
    }

    @Test
    public void writeChunk()throws Exception {
        byte[] model = {11, 22, 33, 44, 55, 66, 77, 88, 99};
        String inFilePath  = "src/test/resources/TestFile.txt";
        String outFilePath = "src/test/resources/dummy.enc";
        try(FileManagerForEncryption fileManagerForEncryption =
                    new FileManagerForEncryption(inFilePath, outFilePath, new byte[] {1},new byte[] {1}, Cipher.EncryptionType.AES)) {
            fileManagerForEncryption.writeChunk(model);

        } catch (Exception e) {
            throw e;
        }

        ByteBuffer bbf = ByteBuffer.wrap(Files.readAllBytes(Paths.get(outFilePath)));

        bbf.position(Short.BYTES + Byte.BYTES);
        int headerSize = bbf.getInt();
        byte[] dummy = new byte[headerSize];
        bbf.get(dummy, 0, headerSize);
        int size = bbf.getInt();
        byte[] writtenBytes = new byte[size];
        bbf.get(writtenBytes, 0, size);

        assertTrue(Arrays.equals(writtenBytes, model));

    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/dummy.enc"));
    }
}