package com.rootnetsec.cryptofile.cipher;

import java.io.IOException;
/**
 * The exception that is thrown when an input file has invalid short header
 */
public class InvalidHeaderException extends IOException {
    private static final long serialVersionUID = 1L;
    
    public InvalidHeaderException(String exception) {
        super(exception);
    } 
}