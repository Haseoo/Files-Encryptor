package com.rootnetsec.cryptofile.cipher.filemanager;

import java.io.IOException;

/**
 * The exception that is thrown when an input file has invalid header
 */
public class FileManagerHeaderInvalidException extends IOException {
    
    public FileManagerHeaderInvalidException(String exception) {
        super(exception);
    } 
}