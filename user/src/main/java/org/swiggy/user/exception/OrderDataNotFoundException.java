package org.swiggy.user.exception;

import org.swiggy.exception.OrderException;

public class OrderDataNotFoundException  extends OrderException {
    public OrderDataNotFoundException(final String message) {
        super(message);
    }
}
