package kr.co.okheeokey.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice("kr.co.okheeokey.user")
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UsernameOccupiedException.class)
    public ResponseEntity<?> usernameAlreadyOccupied(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
