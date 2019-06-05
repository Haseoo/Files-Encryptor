package com.rootnetsec.cryptofile.cipher;

import com.rootnetsec.cryptofile.cipher.javaCipher.AESCipher;
import com.rootnetsec.cryptofile.cipher.javaCipher.SerpentCipher;
import com.rootnetsec.cryptofile.cipher.javaCipher.TwofishCipher;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CipherTest {

    @Test
    public void getInstanceEnum() {
        assertEquals(AESCipher.class, Cipher.getInstance(Cipher.EncryptionType.AES).getClass());
        assertEquals(TwofishCipher.class, Cipher.getInstance(Cipher.EncryptionType.TWOFISH).getClass());
        assertEquals(SerpentCipher.class, Cipher.getInstance(Cipher.EncryptionType.SERPENT).getClass());
    }

    @Test
    public void getInstanceFilePath()  throws  IOException{
        assertEquals(AESCipher.class, Cipher.getInstance("src/test/resources/AESTest.enc").getClass());
        assertEquals(TwofishCipher.class, Cipher.getInstance( "src/test/resources/TWOFISHTest.enc").getClass());
        assertEquals(SerpentCipher.class, Cipher.getInstance("src/test/resources/SERPENTTest.enc").getClass());
    }

    @Test(expected = InvalidHeaderException.class)
    public void getInstanceFilePath2() throws IOException {
        Cipher.getInstance("src/test/resources/TestFile.txt");
    }

    @Test
    public void TYPE_PARSE_MAP() {
        assertEquals(Cipher.EncryptionType.AES, Cipher.TYPE_PARSE_MAP.get("AES"));
        assertEquals(Cipher.EncryptionType.TWOFISH, Cipher.TYPE_PARSE_MAP.get("TwoFish"));
        assertEquals(Cipher.EncryptionType.SERPENT, Cipher.TYPE_PARSE_MAP.get("Serpent"));
    }
}