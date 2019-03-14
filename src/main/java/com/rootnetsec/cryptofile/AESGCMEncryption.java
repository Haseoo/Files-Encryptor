package com.rootnetsec.cryptofile;

import java.nio.ByteBuffer;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

public final class AESGCMEncryption {
    private static final String AES_ALGORITHM   = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT     = 128;
    private static final int IV_LENGTH          = 12;

    public static byte[] encryptBytesWithAES(byte[] data, String userKey) {
        ByteBuffer hashBuffer = ByteBuffer.wrap(PBKDF2Hashing.hash(userKey));
        int saltLength = hashBuffer.getInt();
        byte[] salt = new byte[saltLength];
        hashBuffer.get(salt);
        byte[] hash = new byte[hashBuffer.remaining()];
        hashBuffer.get(hash);

        try {
            byte[] iv = RandomBytesGenerator.generate(IV_LENGTH);
            
            final SecretKeySpec key = new SecretKeySpec(hash, "AES");
            GCMParameterSpec parameters = new GCMParameterSpec(128, iv);

            final Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

            byte[] ciphertext = cipher.doFinal(data);

            ByteBuffer outputBuffer = ByteBuffer.allocate(4 + salt.length + 4 + iv.length + ciphertext.length);
            outputBuffer.putInt(saltLength);
            outputBuffer.put(salt);
            outputBuffer.putInt(iv.length);
            outputBuffer.put(iv);
            outputBuffer.put(ciphertext);

            return outputBuffer.array();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static byte[] decryptBytesWithAES(byte[] data, String userKey) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            int saltLength = byteBuffer.getInt();
            byte[] salt = new byte[saltLength];
            byteBuffer.get(salt);
            
            byte[] hash = PBKDF2Hashing.hash(userKey, salt);
        
            int ivLength = byteBuffer.getInt();
            if (ivLength != IV_LENGTH) {
                return null;
            }

            byte[] iv = new byte[ivLength];
            byteBuffer.get(iv);
            byte[] ciphertext = new byte[byteBuffer.remaining()];
            byteBuffer.get(ciphertext);
            
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