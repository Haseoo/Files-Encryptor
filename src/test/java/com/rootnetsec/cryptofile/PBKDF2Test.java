package com.rootnetsec.cryptofile;

import org.junit.Assert;
import org.junit.Test;
import java.nio.ByteBuffer;

public class PBKDF2Test {

    @Test
    public void saltLengthTest() throws Exception {
        ByteBuffer hashBuffer = ByteBuffer.wrap(PBKDF2Hashing.hash("password"));
        int saltLength = hashBuffer.getInt();

        Assert.assertEquals(saltLength, PBKDF2Hashing.SALT_BYTES);
    }
}