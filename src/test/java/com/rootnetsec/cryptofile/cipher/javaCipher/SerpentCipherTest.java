package com.rootnetsec.cryptofile.cipher.javaCipher;

import com.rootnetsec.cryptofile.cipher.filemanager.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SerpentCipherTest {

    @Test
    public void encryptFileAndDecryptFile() throws Exception{
        String inFilePath  = "src/test/resources/TestFile.txt";
        String encFilePath = "src/test/resources/TestFile.enc";
        String outFile     = "src/test/resources/TestFile2.txt";
        String userKey     = "haslo";

        SerpentCipher cipher = new SerpentCipher();
        cipher.encryptFile(inFilePath, encFilePath, userKey);

        cipher = new SerpentCipher();
        cipher.decryptFile(encFilePath, outFile, userKey);

        String outFileContent = new String(Files.readAllBytes(Paths.get(outFile)));
        String modelFileContent = new String(Files.readAllBytes(Paths.get(inFilePath)));
        assertEquals(modelFileContent, outFileContent);

        Files.deleteIfExists(Paths.get(outFile));
        Files.deleteIfExists(Paths.get(encFilePath));


    }

    @Test (expected = Exception.class)
    public void decryptionFailTest1() throws Exception{
        String encFilePath = "src/test/resources/TWOFISHTest.enc";
        String outFile     = "src/test/resources/TestFile2.txt";
        String userKey     = "hasloooo";

        SerpentCipher cipher = new SerpentCipher();
        cipher.decryptFile(encFilePath, outFile, userKey);

    }

    @Test (expected = Exception.class)
    public void decryptionFailTest2() throws Exception{
        String encFilePath = "src/test/resources/AESTest.enc";
        String outFile     = "src/test/resources/TestFile2.txt";
        String userKey     = "haslo";

        SerpentCipher cipher = new SerpentCipher();
        cipher.decryptFile(encFilePath, outFile, userKey);

    }

    @Test
    public void getNumberOfChunks() throws Exception{
        SerpentCipher cipher = new SerpentCipher();

        assertEquals(cipher.getNumberOfChunks(), 0);

        String inFilePath    = "src/test/resources/TestFile.txt";
        String encFilePath   = "src/test/resources/TestFile.enc";
        String userKey = "haslo";

        long numberOfModelFileChunks = (long)Math.ceil(new File(inFilePath).length() / (double) FileManager.MAX_CHUNK_SIZE);

        cipher.encryptFile(inFilePath, encFilePath, userKey);

        assertEquals(numberOfModelFileChunks, cipher.getNumberOfChunks());
        Files.deleteIfExists(Paths.get(encFilePath));
    }

    @Test
    public void isWorkDoneEncryption() throws Exception{
        SerpentCipher cipher = new SerpentCipher();

        assertEquals(cipher.getNumberOfChunks(), 0);

        String inFilePath    = "src/test/resources/TestFile.txt";
        String encFilePath   = "src/test/resources/TestFile.enc";
        String userKey = "haslo";

        assertFalse(cipher.isWorkDone());
        cipher.encryptFile(inFilePath, encFilePath, userKey);
        assertTrue(cipher.isWorkDone());
        Files.deleteIfExists(Paths.get(encFilePath));

    }


    @Test
    public void isWorkDoneDecryption() throws Exception{
        SerpentCipher cipher = new SerpentCipher();

        assertEquals(cipher.getNumberOfChunks(), 0);

        String encFilePath    = "src/test/resources/SERPENTTest.enc";
        String outFilePath   = "src/test/resources/TestFile2.txt";
        String userKey = "haslo";

        assertFalse(cipher.isWorkDone());
        cipher.decryptFile(encFilePath,outFilePath, userKey);
        assertTrue(cipher.isWorkDone());
        Files.deleteIfExists(Paths.get(outFilePath));

    }

    @Test
    public void getCurrentChunkEncryption() throws Exception{
        SerpentCipher cipher = new SerpentCipher();

        assertEquals(cipher.getNumberOfChunks(), 0);

        String inFilePath    = "src/test/resources/TestFile.txt";
        String encFilePath   = "src/test/resources/TestFile.enc";
        String userKey = "haslo";

        long currentChunk = 0;

        Thread th = new Thread(() -> {
            try {
                cipher.encryptFile(inFilePath, encFilePath, userKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        th.start();

        while (!cipher.isWorkDone()) {
            assertFalse(currentChunk < cipher.getCurrentChunk());
            assertFalse(cipher.getCurrentChunk() > cipher.getNumberOfChunks());
            currentChunk = cipher.getCurrentChunk();
        }

        th.join();
        Files.deleteIfExists(Paths.get(encFilePath));
    }

    @Test
    public void getCurrentChunkDecryption() throws Exception{
        SerpentCipher cipher = new SerpentCipher();

        assertEquals(cipher.getNumberOfChunks(), 0);

        String encFilePath    = "src/test/resources/SERPENTTest.enc";
        String outFilePath   = "src/test/resources/TestFile2.txt";
        String userKey = "haslo";

        long currentChunk = 0;

        Thread th = new Thread(() -> {
            try {
                cipher.decryptFile(encFilePath, outFilePath, userKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        th.start();

        while (!cipher.isWorkDone()) {
            assertFalse(currentChunk < cipher.getCurrentChunk());
            assertFalse(cipher.getCurrentChunk() > cipher.getNumberOfChunks());
            currentChunk = cipher.getCurrentChunk();
        }

        th.join();
        Files.deleteIfExists(Paths.get(outFilePath));
    }

    @AfterClass
    public static void after() throws Exception {
        Files.deleteIfExists(Paths.get("TestFile2.txt"));
        Files.deleteIfExists(Paths.get("TestFile.enc"));
    }
}
