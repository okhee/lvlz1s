package kr.co.okheeokey.user.exception;

public class UsernameOccupiedException extends RuntimeException {
    public UsernameOccupiedException() {
    }

    public UsernameOccupiedException(String message) {
        super(message);
    }
}
