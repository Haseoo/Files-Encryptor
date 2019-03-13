package com.rootnetsec.cryptofile;

import java.util.Arrays;
import java.nio.ByteBuffer;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

public class Crypto {
    private static final int TAG_LENGTH_BIT = 128;
    private static final int MIN_IV_LENGTH = 12;
    private static final int MAX_IV_LENGTH = 16;

    private byte[] CreateHash(String user_key) {
        return CreateHash(user_key.toCharArray());
    }

    private byte[] CreateHash(char[] user_key) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[24];
            random.nextBytes(salt);

            PBEKeySpec key = new PBEKeySpec(user_key, salt, 1000, 24 * 8);
            SecretKeyFactory secret_key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = secret_key.generateSecret(key).getEncoded();

            ByteBuffer byte_buffer = ByteBuffer.allocate(4 + 24 + hash.length);
            byte_buffer.putInt(salt.length);
            byte_buffer.put(salt);
            byte_buffer.put(hash);

            return byte_buffer.array();

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private byte[] CreateHashWithSalt(String user_key, byte[] salt) {
        return CreateHashWithSalt(user_key.toCharArray(), salt);
    }

    private byte[] CreateHashWithSalt(char[] user_key, byte[] salt) {
        try {
            PBEKeySpec key = new PBEKeySpec(user_key, salt, 1000, 24 * 8);
            SecretKeyFactory secret_key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = secret_key.generateSecret(key).getEncoded();
            return hash;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public byte[] EncryptBytes(byte[] plaintext, String user_key) {
        ByteBuffer hash_buffer = ByteBuffer.wrap(CreateHash(user_key));
        int salt_length = hash_buffer.getInt();
        byte[] salt = new byte[salt_length];
        hash_buffer.get(salt);
        byte[] hash = new byte[hash_buffer.remaining()];
        hash_buffer.get(hash);

        try {
            SecureRandom secureRandom = new SecureRandom();

            byte[] iv = new byte[MIN_IV_LENGTH];
            secureRandom.nextBytes(iv);

            final SecretKeySpec key = new SecretKeySpec(hash, "AES");
            GCMParameterSpec parameters = new GCMParameterSpec(128, iv);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

            byte[] ciphertext = cipher.doFinal(plaintext);

            ByteBuffer byte_buffer = ByteBuffer.allocate(4 + salt.length + 4 + iv.length + ciphertext.length);
            byte_buffer.putInt(salt_length);
            byte_buffer.put(salt);
            byte_buffer.putInt(iv.length);
            byte_buffer.put(iv);
            byte_buffer.put(ciphertext);

            //Arrays.fill(user_key, (byte)0);
            //Arrays.fill(iv, (byte)0);

            return byte_buffer.array();

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public byte[] DecryptBytes(byte[] encrypted_data, String user_key) {
        try {
            ByteBuffer byte_buffer = ByteBuffer.wrap(encrypted_data);
            int salt_length = byte_buffer.getInt();
            byte[] salt = new byte[salt_length];
            byte_buffer.get(salt);
            
            byte[] hash = CreateHashWithSalt(user_key, salt);
        
            int iv_length = byte_buffer.getInt();
            if (iv_length < MIN_IV_LENGTH || iv_length >= MAX_IV_LENGTH) {
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
        }

        return null;
    }


}