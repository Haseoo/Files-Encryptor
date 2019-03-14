package com.rootnetsec.cryptofile;

import java.security.SecureRandom;

public final class Random {
    public static byte[] GenerateRandomBytes(int size) {
        byte[] random_bytes = new byte[size];
        SecureRandom secure_random = new SecureRandom();
        secure_random.nextBytes(random_bytes);
        return random_bytes; 
    }
}