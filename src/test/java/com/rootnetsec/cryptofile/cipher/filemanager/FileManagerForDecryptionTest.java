package com.rootnetsec.cryptofile.cipher.filemanager;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        byte[] model = {-108, -18, 12, -54, -35, 103, -94, -17, -62, -17, 35, -40, 122, -3, -50, 20,
                        72, 79, 75, 59, 110, -116, 63, 64, -81, -106, -10, -84, -44, -20, -62, -118};
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
        byte[] model = {-47, -116, -23, 50, 20, 109, 96, -84, -52, 67, 34, -98, 98, 103, 85, -94};
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            assertArrayEquals(fileManagerForDecryption.getIV(), model);

        }
    }

    @Test
    public void getSalt()throws Exception {
        String inFilePath  = "src/test/resources/AESTest.enc";
        String outFilePath = "src/test/resources/dummy.txt";
        byte[] model = {-96, -56, 114, -17, 104, 77, 1, -50, 35, -13, 115, 34, 110, 103, 108, -112, 94, -31, -91, -61, -125, -93, 73, -90};
        try (FileManagerForDecryption fileManagerForDecryption = new FileManagerForDecryption(inFilePath, outFilePath)) {

            assertArrayEquals(fileManagerForDecryption.getSalt(), model);

        }
    }

    @After public void after() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/dummy.txt"));
    }
}