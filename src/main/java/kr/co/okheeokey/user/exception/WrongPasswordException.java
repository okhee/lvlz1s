package kr.co.okheeokey.user.exception;

import org.springframework.security.core.AuthenticationException;

public class WrongPasswordException extends AuthenticationException {

    public WrongPasswordException(String message) {
        super(message);
    }
}
