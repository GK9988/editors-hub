package org.garvk.apigateway.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
