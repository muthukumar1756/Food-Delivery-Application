package org.swiggy;

public class CartException extends RuntimeException {
    public CartException(final String message) {
        super(message);
    }
}