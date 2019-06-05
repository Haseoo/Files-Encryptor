package com.rootnetsec.cryptofile.cipher.filemanager;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileManagerForDecryptionTest {

    @Test (expected = Exception.class)
    public void constructorExceptionTest1() throws Exception{
        String inFilePath  = "src/test/resources/dontExist.txt";
        String outFilePath = "src/test/resources/dummy.txt";
        new FileManagerForDecryption(inFilePath, outFilePath);
    }

    @Test (expected = FileManagerHeaderInvalidException.class)
    public void constructorExceptionTest2() throws Exception{
        String inFilePath  = "src/test/resources/testFile.txt";
        String outFilePath = "src/test/resources/dummy.txt";
        new FileManagerForDecryption(inFilePath, outFilePath);
    }

    @Test (expected = FileManagerHeaderInvalidException.class)
    public void constructorExceptionTestInvalidIV() throws Exception{
        String inFilePath  = "src/test/resources/InvalidIV.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        new FileManagerForDecryption(inFilePath, outFilePath);
    }

    @Test (expected = FileManagerHeaderInvalidException.class)
    public void constructorExceptionTestInvalidSalt() throws Exception{
        String inFilePath  = "src/test/resources/InvalidSalt.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        new FileManagerForDecryption(inFilePath, outFilePath);
    }

    @Test
    public void getNumberOfChunks() throws Exception {
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        try(FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            long numberOfModelFileChunks = (long) Math.ceil(new File(inFilePath).length() / (double) FileManager.MAX_CHUNK_SIZE);
            assertEquals(numberOfModelFileChunks, fileManagerForDecryption.getNumberOfChunks());
        }
    }

    @Test
    public void getChunk() throws Exception{
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        byte[] model = {-60, -63, 30, 118, 104, -15, 2, 34, -55, 56, -52, -51, 5, 42, 6, 127, 40, 50, 6, 61, 119,
                        -33, 127, 108, 102, -123, -115, 110, 85, 46, 19, 83};
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            assertArrayEquals(fileManagerForDecryption.getChunk(), model);

        }
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getChunkException() throws Throwable {
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            fileManagerForDecryption.getChunk();
            fileManagerForDecryption.getChunk();

        }
    }

    @Test
    public void writeChunk()throws Exception {
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        byte[] model = {11, 22, 33, 44, 55, 66, 77, 88, 99};
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            fileManagerForDecryption.writeChunk(model);

        }

        byte[] writtenBytes = Files.readAllBytes(Paths.get(outFilePath));

        assertArrayEquals(writtenBytes, model);
    }

    @Test
    public void getIV() throws Exception{
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        byte[] model = {-106, 35, -9, 25, -56, 67, 124, -45, -116, -118, -116, 125, 57, 90, 22, -6};
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            assertArrayEquals(fileManagerForDecryption.getIV(), model);

        }
    }

    @Test
    public void getSalt()throws Exception {
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        byte[] model = {-73, 62, 81, 3, -106, -14, -68, -14, -4, -63, 2, -114, -30, -60, 124, -57, -73, -30, 7, 5, -12, -19, 75, -59};
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            //assertArrayEquals(fileManagerForDecryption.getSalt(), model);

        }
    }

    @After public void after() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/dummy.txt"));
    }
}