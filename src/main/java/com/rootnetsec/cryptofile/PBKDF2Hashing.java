package com.rootnetsec.cryptofile;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

/**
 * Class uses PBKDF2 key derivation algorithm with SHA1 to hash the user specified password.
 */

public final class PBKDF2Hashing {
    public static final int SALT_BYTES              = 24;
    private static final String PBKDF2_ALGORITHM    = "PBKDF2WithHmacSHA1";
    private static final int HASH_BYTES             = 32;
    private static final int HASH_ITERATIONS        = 10000;

    /** Hash the user supplied data
     * @param data Data to hash.
     * @return Byte array of derived hash.
     */

    public static byte[] hash(String data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] char_data = data.toCharArray();
        byte[] salt = RandomBytesGenerator.generate(SALT_BYTES);
        PBEKeySpec key = new PBEKeySpec(char_data, salt, HASH_ITERATIONS, HASH_BYTES * 8);
        SecretKeyFactory secret_key = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] hash = secret_key.generateSecret(key).getEncoded();

        ByteBuffer byte_buffer = ByteBuffer.allocate(4 + SALT_BYTES + hash.length);
        byte_buffer.putInt(salt.length);
        byte_buffer.put(salt);
        byte_buffer.put(hash);

        return byte_buffer.array();

    }

    /** Hash the user supplied data with provided salt.
     * @param data Data to hash.
     * @param data Salt used in hashing.
     * @return Byte array of derived hash.
     */

    public static byte[] hash(String data, byte[] salt) throws GeneralSecurityException {
        char[] char_data = data.toCharArray();
        PBEKeySpec key = new PBEKeySpec(char_data, salt, HASH_ITERATIONS, HASH_BYTES * 8);
        SecretKeyFactory secretKey = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] hash = secretKey.generateSecret(key).getEncoded();
        
        return hash;

    }
}