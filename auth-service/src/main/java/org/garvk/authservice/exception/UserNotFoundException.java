package org.garvk.authservice.exception;

public class UserNotFoundException extends AuthServiceException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
