package com.rootnetsec.cryptofile;

import java.security.SecureRandom;

/**
 * Class that generates safe random bytes using java.security.SecureRandom
 */
public final class RandomBytesGenerator {
    /** Generates safe random bytes
     * @param size Number of bytes to generate.
     * @return Array of random bytes with given size.
     */
    public static byte[] generate(int size) {
        byte[] random_bytes = new byte[size];
        SecureRandom secure_random = new SecureRandom();
        secure_random.nextBytes(random_bytes);
        return random_bytes; 
    }
}