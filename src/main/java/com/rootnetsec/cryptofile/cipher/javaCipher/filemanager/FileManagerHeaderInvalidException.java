package com.rootnetsec.cryptofile.cipher.javaCipher.filemanager;

import java.io.IOException;

public class FileManagerHeaderInvalidException extends IOException {
    private static final long serialVersionUID = 1L;
    
    public FileManagerHeaderInvalidException(String exception) {
        super(exception);
    } 
}