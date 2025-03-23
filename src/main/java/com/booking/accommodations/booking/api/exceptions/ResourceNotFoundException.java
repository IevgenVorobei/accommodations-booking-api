package com.booking.accommodations.booking.api.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String resourceId) {
        super(String.format("No %s was found for id: %s", resourceName, resourceId));
    }
}