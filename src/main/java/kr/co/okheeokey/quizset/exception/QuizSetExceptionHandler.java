package kr.co.okheeokey.quizset.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice("kr.co.okheeokey.quizset")
public class QuizSetExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> invalidSongFileId(Exception e) {
        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("message", e.getMessage()));
    }

}
