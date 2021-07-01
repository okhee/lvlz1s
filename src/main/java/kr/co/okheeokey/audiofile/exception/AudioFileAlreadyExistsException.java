package kr.co.okheeokey.audiofile.exception;

public class AudioFileAlreadyExistsException extends RuntimeException {
    public AudioFileAlreadyExistsException(String message) {
        super(message);
    }
}
