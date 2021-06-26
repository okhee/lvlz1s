package kr.co.okheeokey.auth.exception;

public class CustomJwtRuntimeException extends RuntimeException {
    public CustomJwtRuntimeException() {
        super("Authentication Failed");
    }

    public CustomJwtRuntimeException(Exception ex) {
        super(ex);
    }
}
