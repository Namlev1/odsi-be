package com.odsi.be.exceptions;

public class InvalidPublicKeyException extends RuntimeException {
    public InvalidPublicKeyException(String message) {
        super(message);
    }
}

