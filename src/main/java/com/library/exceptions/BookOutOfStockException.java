package com.library.exceptions;

public class BookOutOfStockException extends Exception {

    public BookOutOfStockException(String msg) {
        super(msg);
    }
}