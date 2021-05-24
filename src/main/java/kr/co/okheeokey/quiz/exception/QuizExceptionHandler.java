package kr.co.okheeokey.quiz.exception;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.NoSuchElementException;

@ControllerAdvice("kr.co.okheeokey.quiz")
public class QuizExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<?> questionOutOfBounds(Exception e) {
        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> userNotAllowed(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noElementExists(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}