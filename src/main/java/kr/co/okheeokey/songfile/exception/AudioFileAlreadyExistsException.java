package kr.co.okheeokey.songfile.exception;

public class AudioFileAlreadyExistsException extends RuntimeException{
    public AudioFileAlreadyExistsException(String message) {
        super(message);
    }
}
