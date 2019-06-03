package com.rootnetsec.cryptofile;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomBytesGeneratorTest {

    @Test
    public void generate() {
        ArrayList<byte[]> bytes = new ArrayList<>();
        final int NUMBER_OF_ITERATIONS = 3000;
        final int NUMBER_OF_BYTES = 100;

        for(int i = 0;i < NUMBER_OF_ITERATIONS; ++i) {
            bytes.add(RandomBytesGenerator.generate(NUMBER_OF_BYTES));
        }

        for(int i = 0; i < bytes.size(); i++) {
            for(int j = i + 1; j < bytes.size(); j++) {
                Assert.assertFalse (Arrays.equals(bytes.get(i), bytes.get(j)));
            }
        }


    }
}