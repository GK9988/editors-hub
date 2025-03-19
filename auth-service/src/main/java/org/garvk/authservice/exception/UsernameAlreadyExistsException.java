package org.garvk.authservice.exception;

public class UsernameAlreadyExistsException extends AuthServiceException{
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists" + username);
    }
}
