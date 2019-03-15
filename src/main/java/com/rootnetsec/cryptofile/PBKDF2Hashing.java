package com.rootnetsec.cryptofile;

import java.util.Arrays;
import java.nio.ByteBuffer;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

public final class PBKDF2Hashing {
    private static final String PBKDF2_ALGORITHM    = "PBKDF2WithHmacSHA1";
    private static final int SALT_BYTES             = 24;
    private static final int HASH_BYTES             = 32;
    private static final int HASH_ITERATIONS        = 10000;

    public static byte[] hash(String data) {
        char[] char_data = data.toCharArray();
        try {
            byte[] salt = RandomBytesGenerator.generate(SALT_BYTES);
            PBEKeySpec key = new PBEKeySpec(char_data, salt, HASH_ITERATIONS, HASH_BYTES * 8);
            SecretKeyFactory secret_key = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            byte[] hash = secret_key.generateSecret(key).getEncoded();

            ByteBuffer byte_buffer = ByteBuffer.allocate(4 + SALT_BYTES + hash.length);
            byte_buffer.putInt(salt.length);
            byte_buffer.put(salt);
            byte_buffer.put(hash);

            return byte_buffer.array();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static byte[] hash(String data, byte[] salt) {
        char[] char_data = data.toCharArray();
        try {
            PBEKeySpec key = new PBEKeySpec(char_data, salt, HASH_ITERATIONS, HASH_BYTES * 8);
            SecretKeyFactory secretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = secretKey.generateSecret(key).getEncoded();
            
            return hash;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}