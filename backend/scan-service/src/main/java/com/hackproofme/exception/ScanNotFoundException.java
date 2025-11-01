package com.hackproofme.exception;

public class ScanNotFoundException extends RuntimeException {
    public ScanNotFoundException(String message) {
        super(message);
    }
}
