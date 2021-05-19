package kr.co.okheeokey.songfile.exception;

import kr.co.okheeokey.songfile.controller.SongFileController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Collections;

@ControllerAdvice("kr.co.okheeokey.songfile")
public class SongFileExceptionHandler extends ResponseEntityExceptionHandler {
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

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
