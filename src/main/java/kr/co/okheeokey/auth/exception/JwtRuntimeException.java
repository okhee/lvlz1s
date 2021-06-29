package kr.co.okheeokey.auth.exception;

import io.jsonwebtoken.JwtException;

public class JwtRuntimeException extends JwtException {
    public JwtRuntimeException(String message) {
        super(message);
    }
}
