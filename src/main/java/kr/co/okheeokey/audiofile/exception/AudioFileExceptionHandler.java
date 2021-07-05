package kr.co.okheeokey.audiofile.exception;

import com.sun.media.sound.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.IllegalFormatException;

@ControllerAdvice("kr.co.okheeokey.audiofile")
public class AudioFileExceptionHandler {
    @ExceptionHandler(value = NoAudioFileExistsException.class)
    public ResponseEntity<?> noAudioFileExist(NoAudioFileExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(value = AudioFileAlreadyExistsException.class)
    public ResponseEntity<?> audioFileAlreadyExist(AudioFileAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(value = GeneralSecurityException.class)
    public ResponseEntity<?> generalSecurityException(GeneralSecurityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public ResponseEntity<?> invalidAudioFormatException(IllegalFormatException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}