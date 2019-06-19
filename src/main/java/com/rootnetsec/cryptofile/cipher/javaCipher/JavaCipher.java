package com.rootnetsec.cryptofile.cipher.javaCipher;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.RandomBytesGenerator;
import com.rootnetsec.cryptofile.cipher.DecryptionError;
import com.rootnetsec.cryptofile.cipher.EncryptionError;
import com.rootnetsec.cryptofile.cipher.filemanager.FileManager;
import com.rootnetsec.cryptofile.cipher.filemanager.FileManagerForDecryption;
import com.rootnetsec.cryptofile.cipher.filemanager.FileManagerForEncryption;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;


/**
 * The abstract class for java library encryption algorithms
 */
abstract public class JavaCipher extends com.rootnetsec.cryptofile.cipher.Cipher {
    /**
     * String used to get java library cipher instance
     */
    protected final String keyString;
    /**
     * String used to get java library cipher instance
     */
    protected final String transformation;
    /**
     * String used to get java library cipher instance
     */
    protected final String provider;
    /**
     * Value that indicates used encryption algorithm
     */
    protected final com.rootnetsec.cryptofile.cipher.Cipher.EncryptionType type;
    /**
     * The IV size in bytes
     */
    public static final int IV_LENGTH = 16;

    /** Allows to create an object that is capable to encrypt/decrypt file
     * @param keyString value "AES", "TWOFISH" or "SERPENT"
     * @param transformation string that identifies encryption algorithm
     * @param provider algorithm provider
     * @param type enum algorithm value
     */
    protected JavaCipher(String keyString,
                         String transformation,
                         String provider,
                         com.rootnetsec.cryptofile.cipher.Cipher.EncryptionType type ){

        this.keyString      = keyString;
        this.transformation = transformation;
        this.provider       = provider;
        this.type           = type;
    }

    /**
     * Unused, private constructor that sets object fields to null
     */
    @SuppressWarnings("unused")
    private JavaCipher() {
        this.keyString      = null;
        this.transformation = null;
        this.provider       = null;
        this.type           = null;
    }

    /** Reads chunks form input file, encrypt/decrypt this chunk and writes it in output file
     * @param fileManager a fileManager class instance
     * @param cipher an java library cipher instance
     * @throws IOException thrown when an IO error occurred
     * @throws GeneralSecurityException thrown when an encryption error occurred
     */
    private void iterate(FileManager fileManager, Cipher cipher) throws IOException, GeneralSecurityException{
        for (int i = 0; i < numberOfChunks - 1; i++) {
            byte[] tmp = fileManager.getChunk();
            byte[] toWrite = cipher.update(tmp);
            fileManager.writeChunk(toWrite);
            setCurrentChunk(i);
        }
        byte[] tmp = fileManager.getChunk();
        byte[] toWrite = cipher.doFinal(tmp);
        fileManager.writeChunk(toWrite);
    }

    /** Encrypts a file using java library cipher
     * @param srcFile input file path
     * @param destFile output file path
     * @param userKey user password
     * @throws GeneralSecurityException Thrown when an encryption error occurred
     */
    @Override
    public void encryptFile(String srcFile, String destFile, String userKey) throws GeneralSecurityException {
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

            iterate(fileManager, cipher);

        } catch (Exception e) {
            EncryptionError th = new EncryptionError(e.getMessage());
            throw th;
        }
        finally {
            setWorkDone();
        }
    }

    /** Decrypts a file using java library cipher
     * @param srcFile input file path
     * @param destFile output file path
     * @param userKey user password
     * @throws DecryptionError Thrown when an encryption error occurred
     */
    public void decryptFile(String srcFile, String destFile, String userKey) throws DecryptionError {
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

            iterate(fileManager, cipher);

        } catch (Exception e) {
            DecryptionError th = new DecryptionError(e.getMessage());
            throw th;
        }
        finally {
            setWorkDone();
        }
    }
}