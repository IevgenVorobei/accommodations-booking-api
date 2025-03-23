package com.booking.accommodations.booking.api.exceptions;

public class OrderConfirmationFailedException extends RuntimeException {

    public OrderConfirmationFailedException(String orderId, String reason) {
        super(String.format("Order with id %s was failed to be confirmed, reason: %s", orderId, reason));
    }

}
