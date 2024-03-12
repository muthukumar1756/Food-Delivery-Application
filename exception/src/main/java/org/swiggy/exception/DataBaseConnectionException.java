package org.swiggy.exception;

public class DataBaseConnectionException extends RuntimeException {
    public DataBaseConnectionException(final String message) {
        super(message);
    }
}
