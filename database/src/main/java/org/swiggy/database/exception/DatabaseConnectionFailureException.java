package org.swiggy.database.exception;

import org.swiggy.exception.DataBaseConnectionException;

public class DatabaseConnectionFailureException extends DataBaseConnectionException {
    public DatabaseConnectionFailureException(final String message) {
        super(message);
    }
}

