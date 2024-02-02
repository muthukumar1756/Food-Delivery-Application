package org.swiggy.exception;

import org.swiggy.DataBaseConnectionException;

public class DatabaseConnectionFailureException extends DataBaseConnectionException {
    public DatabaseConnectionFailureException(final String message) {
        super(message);
    }
}

