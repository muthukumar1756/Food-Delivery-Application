package org.swiggy.exception;

public class OrderException extends RuntimeException {
    public OrderException(final String message) {
        super(message);
    }
}
