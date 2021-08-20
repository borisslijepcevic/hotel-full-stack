package com.boris.hotelmanagementfullstack.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String message) {
        super(message);
    }
}
