package com.rootnetsec.cryptofile;

import java.nio.ByteBuffer;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.rootnetsec.filemanager.*;

public final class AesCipher {
    private static final String AES_ALGORITHM   = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT     = 128;
    public static final int IV_LENGTH          = 16;


    public static void encryptFile(String userKey) {
        ByteBuffer hashBuffer = ByteBuffer.wrap(PBKDF2Hashing.hash(userKey));
        int saltLength = hashBuffer.getInt();
        byte[] salt = new byte[saltLength];
        hashBuffer.get(salt);
        byte[] hash = new byte[hashBuffer.remaining()];
        hashBuffer.get(hash);

        byte[] iv = RandomBytesGenerator.generate(IV_LENGTH);

        try {
            FileManagerForEncryption fileManager = new FileManagerForEncryption("README.md", "README.enc", salt, iv);

            final SecretKeySpec key = new SecretKeySpec(hash, "AES");
            IvParameterSpec parameters = new IvParameterSpec(iv);

            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);
            
            for (int i = 0; i < fileManager.getNumberOfChunks(); i++) {
                byte[] tmp = fileManager.getChunk();
                if (i == fileManager.getNumberOfChunks() - 1) {
                    byte[] toWrite = cipher.doFinal(tmp);
                    fileManager.writeChunk(toWrite);
                    break;
                }
                //System.out.println(new String(tmp));
                byte[] toWrite = cipher.update(tmp);
                //System.out.println(new String(toWrite));
                fileManager.writeChunk(toWrite);
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void decryptFile(String userKey) {
        try {
            FileManagerForDecryption fileManager = new FileManagerForDecryption("README.enc", "tmp.txt");

            byte[] salt = fileManager.getSalt();
            byte[] hash = PBKDF2Hashing.hash(userKey, salt);

            byte[] iv = fileManager.getIV();

            final SecretKeySpec key = new SecretKeySpec(hash, "AES");
            IvParameterSpec parameters = new IvParameterSpec(iv);

            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, key, parameters);

            for (int i = 0; i < fileManager.getNumberOfChunks(); i++) {
                byte[] tmp = fileManager.getChunk();
                if (i == fileManager.getNumberOfChunks() - 1) {
                    byte[] toWrite = cipher.doFinal(tmp);
                    fileManager.writeChunk(toWrite);
                    break;
                }

                byte[] toWrite = cipher.update(tmp);
                fileManager.writeChunk(toWrite);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}