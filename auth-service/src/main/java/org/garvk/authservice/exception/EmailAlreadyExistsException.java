package org.garvk.authservice.exception;

public class EmailAlreadyExistsException extends AuthServiceException{
    public EmailAlreadyExistsException(String email) {
        super("Email already in use: " + email);
    }
}
