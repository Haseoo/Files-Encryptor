package com.rootnetsec.cryptofile.cipher.javaCipher;

import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

import com.rootnetsec.cryptofile.PBKDF2Hashing;
import com.rootnetsec.cryptofile.RandomBytesGenerator;
import com.rootnetsec.cryptofile.cipher.javaCipher.filemanager.FileManagerForDecryption;
import com.rootnetsec.cryptofile.cipher.javaCipher.filemanager.FileManagerForEncryption;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

abstract public class JavaSTDCipher extends com.rootnetsec.cryptofile.cipher.Cipher {
    protected final String keyString;
    protected final String transformation;
    protected final String provider;
    public static final int IV_LENGTH = 16;

    protected JavaSTDCipher(String keyString, String transformation, String provider){
        this.keyString      = keyString;
        this.transformation = transformation;
        this.provider       = provider;
    }

    @SuppressWarnings("unused")
    private JavaSTDCipher() {
        this.keyString      = null;
        this.transformation = null;
        this.provider       = null;
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
        try (FileManagerForEncryption fileManager = new FileManagerForEncryption(srcFile, destFile, salt, iv)) {

            final SecretKeySpec key = new SecretKeySpec(hash, keyString);
            IvParameterSpec parameters = new IvParameterSpec(iv);

            final Cipher cipher = Cipher.getInstance(transformation, provider);
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
            Exception th = new Exception("Encyryption error: " + e.getMessage());
            throw th;
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
            Exception th = new Exception("Decyryption error: " + e);
            throw th;
        }
    }
}