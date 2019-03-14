package com.rootnetsec.cryptofile;

import java.nio.ByteBuffer;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

public final class Crypto {
    private static final String AES_ALGORITHM   = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT     = 128;
    private static final int IV_LENGTH          = 12;

    public static byte[] EncryptBytesWithAES(byte[] plaintext, String user_key) {
        ByteBuffer hash_buffer = ByteBuffer.wrap(Hash.HashWithPBKDF2(user_key));
        int salt_length = hash_buffer.getInt();
        byte[] salt = new byte[salt_length];
        hash_buffer.get(salt);
        byte[] hash = new byte[hash_buffer.remaining()];
        hash_buffer.get(hash);

        try {
            byte[] iv = Random.GenerateRandomBytes(IV_LENGTH);
            
            final SecretKeySpec key = new SecretKeySpec(hash, "AES");
            GCMParameterSpec parameters = new GCMParameterSpec(128, iv);

            final Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

            byte[] ciphertext = cipher.doFinal(plaintext);

            ByteBuffer byte_buffer = ByteBuffer.allocate(4 + salt.length + 4 + iv.length + ciphertext.length);
            byte_buffer.putInt(salt_length);
            byte_buffer.put(salt);
            byte_buffer.putInt(iv.length);
            byte_buffer.put(iv);
            byte_buffer.put(ciphertext);

            return byte_buffer.array();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static byte[] DecryptBytesWithAES(byte[] encrypted_data, String user_key) {
        try {
            ByteBuffer byte_buffer = ByteBuffer.wrap(encrypted_data);
            int salt_length = byte_buffer.getInt();
            byte[] salt = new byte[salt_length];
            byte_buffer.get(salt);
            
            byte[] hash = Hash.HashWithPBKDF2(user_key, salt);
        
            int iv_length = byte_buffer.getInt();
            if (iv_length != IV_LENGTH) {
                return null;
            }

            byte[] iv = new byte[iv_length];
            byte_buffer.get(iv);
            byte[] ciphertext = new byte[byte_buffer.remaining()];
            byte_buffer.get(ciphertext);
            
            final SecretKeySpec key = new SecretKeySpec(hash, "AES");
            GCMParameterSpec parameters = new GCMParameterSpec(128, iv);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
            byte[] plaintext = cipher.doFinal(ciphertext);
            return plaintext;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


}