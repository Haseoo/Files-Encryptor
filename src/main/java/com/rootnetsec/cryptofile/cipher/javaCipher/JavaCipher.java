package com.rootnetsec.cryptofile.cipher.javaCipher;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Security;

import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.RandomBytesGenerator;
import com.rootnetsec.cryptofile.cipher.filemanager.FileManagerForDecryption;
import com.rootnetsec.cryptofile.cipher.filemanager.FileManagerForEncryption;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

abstract public class JavaCipher extends com.rootnetsec.cryptofile.cipher.Cipher {
    protected final String keyString;
    protected final String transformation;
    protected final String provider;
    protected final com.rootnetsec.cryptofile.cipher.Cipher.EncryptionType type;
    public static final int IV_LENGTH = 16;

    protected JavaCipher(String keyString,
                         String transformation,
                         String provider,
                         com.rootnetsec.cryptofile.cipher.Cipher.EncryptionType type ){

        this.keyString      = keyString;
        this.transformation = transformation;
        this.provider       = provider;
        this.type           = type;
    }

    @SuppressWarnings("unused")
    private JavaCipher() {
        this.keyString      = null;
        this.transformation = null;
        this.provider       = null;
        this.type           = null;
    }

    @Override
    public void encryptFile(String srcFile, String destFile, String userKey) throws Exception {
        ByteBuffer hashBuffer = ByteBuffer.wrap(PBKDF2Hashing.hash(userKey));
        int saltLength = hashBuffer.getInt();
        byte[] salt = new byte[saltLength];
        hashBuffer.get(salt);
        byte[] hash = new byte[hashBuffer.remaining()];
        hashBuffer.get(hash);

        byte[] iv = RandomBytesGenerator.generate(IV_LENGTH);

        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try (FileManagerForEncryption fileManager = new FileManagerForEncryption(srcFile, destFile, salt, iv, type)) {

            final SecretKeySpec key = new SecretKeySpec(hash, keyString);
            IvParameterSpec parameters = new IvParameterSpec(iv);

            final Cipher cipher = Cipher.getInstance(transformation, provider);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

            numberOfChunks = fileManager.getNumberOfChunks();
            
            for (int i = 0; i < numberOfChunks; i++) {
                byte[] tmp = fileManager.getChunk();
                if (i == fileManager.getNumberOfChunks() - 1) {
                    byte[] toWrite = cipher.doFinal(tmp);
                    fileManager.writeChunk(toWrite);
                    break;
                }
                byte[] toWrite = cipher.update(tmp);
                fileManager.writeChunk(toWrite);
                setCurrentChunk(i);
            }
        } catch (Exception e) {
            Exception th = new Exception("Encryption error: " + e.getMessage());
            throw th;
        }
        finally {
            setWorkDone();
        }
    }

    public void decryptFile(String srcFile, String destFile, String userKey) throws Exception {
        try (FileManagerForDecryption fileManager = new FileManagerForDecryption(srcFile, destFile)) {

            byte[] salt = fileManager.getSalt();
            byte[] hash = PBKDF2Hashing.hash(userKey, salt);

            byte[] iv = fileManager.getIV();

            final SecretKeySpec key = new SecretKeySpec(hash, keyString);
            IvParameterSpec parameters = new IvParameterSpec(iv);

            Security.setProperty("crypto.policy", "unlimited");
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            final Cipher cipher = Cipher.getInstance(transformation, provider);
            cipher.init(Cipher.DECRYPT_MODE, key, parameters);

            numberOfChunks = fileManager.getNumberOfChunks();

            for (int i = 0; i < numberOfChunks; i++) {
                byte[] tmp = fileManager.getChunk();
                if (i == fileManager.getNumberOfChunks() - 1) {
                    byte[] toWrite = cipher.doFinal(tmp);
                    fileManager.writeChunk(toWrite);
                    break;
                }

                byte[] toWrite = cipher.update(tmp);
                fileManager.writeChunk(toWrite);
                setCurrentChunk(i);
            }

        } catch (Exception e) {
            Exception th = new IOException("Decryption error: " + e);
            throw th;
        }
        finally {
            setWorkDone();
        }
    }
}