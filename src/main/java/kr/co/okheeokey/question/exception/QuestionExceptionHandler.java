package kr.co.okheeokey.question.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;

@ControllerAdvice("kr.co.okheeokey.question")
public class QuestionExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = NoAudioFileExistsException.class)
    public ResponseEntity<?> noAudioFileExist(NoAudioFileExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(value = AudioFileAlreadyExistsException.class)
    public ResponseEntity<?> audioFileAlreadyExist(AudioFileAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<?> noQuestionExist(NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
