package com.rootnetsec.cryptofile;

import java.security.SecureRandom;

public final class RandomBytesGenerator {
    public static byte[] generate(int size) {
        byte[] random_bytes = new byte[size];
        SecureRandom secure_random = new SecureRandom();
        secure_random.nextBytes(random_bytes);
        return random_bytes; 
    }
}