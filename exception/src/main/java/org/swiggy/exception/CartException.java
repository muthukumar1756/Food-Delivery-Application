package org.swiggy.exception;

public class CartException extends RuntimeException {
    public CartException(final String message) {
        super(message);
    }
}