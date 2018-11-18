package com.bockig.crazybackyard.common;

public class ForwardFailedException extends Exception {
    public ForwardFailedException(String message, Exception e) {
        super(message, e);
    }
}
