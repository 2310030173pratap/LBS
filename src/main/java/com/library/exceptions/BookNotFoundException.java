package com.library.exceptions;

public class BookNotFoundException extends Exception {

    public BookNotFoundException(String msg) {
        super(msg);
    }
}