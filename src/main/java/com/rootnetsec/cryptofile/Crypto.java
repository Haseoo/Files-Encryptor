package com.rootnetsec.cryptofile;

import java.util.Arrays;
import java.nio.ByteBuffer;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    public byte[] Encrypt(byte[] plaintext, byte[] user_key) {
        
        try {
            SecureRandom secureRandom = new SecureRandom();

            byte[] iv = new byte[12];
            secureRandom.nextBytes(iv);

            final SecretKeySpec key = new SecretKeySpec(user_key, "AES");
            GCMParameterSpec parameters = new GCMParameterSpec(128, iv);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

            byte[] ciphertext = cipher.doFinal(plaintext);

            ByteBuffer byte_buffer = ByteBuffer.allocate(4 + iv.length + ciphertext.length);
            byte_buffer.putInt(iv.length);
            byte_buffer.put(iv);
            byte_buffer.put(ciphertext);

            Arrays.fill(user_key, (byte)0);
            Arrays.fill(iv, (byte)0);

            return byte_buffer.array();

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public byte[] Decrypt(byte[] encrypted_data, byte[] user_key) {
        try {
            ByteBuffer byte_buffer = ByteBuffer.wrap(encrypted_data);
            int iv_length = byte_buffer.getInt();

            if (iv_length < 12 || iv_length >= 16) {
                return null;
            }

            byte[] iv = new byte[iv_length];
            byte_buffer.get(iv);
            byte[] ciphertext = new byte[byte_buffer.remaining()];
            byte_buffer.get(ciphertext);
            
            final SecretKeySpec key = new SecretKeySpec(user_key, "AES");
            GCMParameterSpec parameters = new GCMParameterSpec(128, iv);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
            byte[] plaintext = cipher.doFinal(ciphertext);
            return plaintext;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }


}