package com.rootnetsec.cryptofile.cipher;

import java.io.IOException;

public class InvalidHeaderException extends IOException {
    private static final long serialVersionUID = 1L;
    
    public InvalidHeaderException(String exception) {
        super(exception);
    } 
}