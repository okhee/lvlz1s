package kr.co.okheeokey.songfile.exception;

public class NoAudioFileExistsException extends RuntimeException {
    public NoAudioFileExistsException(String message) {
        super(message);
    }
}
