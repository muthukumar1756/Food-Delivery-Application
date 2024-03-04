package org.swiggy.exception;

import org.swiggy.OrderException;

public class OrderDataNotFoundException  extends OrderException {
    public OrderDataNotFoundException(final String message) {
        super(message);
    }
}
