package org.garvk.authservice.exception;

public class TokenValidationException extends AuthServiceException{
    public TokenValidationException(String message) {
        super(message);
    }
}
