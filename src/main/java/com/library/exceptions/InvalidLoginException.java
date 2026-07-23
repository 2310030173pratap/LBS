package com.library.exceptions;

public class InvalidLoginException extends Exception {

    public InvalidLoginException(String msg) {
        super(msg);
    }
}