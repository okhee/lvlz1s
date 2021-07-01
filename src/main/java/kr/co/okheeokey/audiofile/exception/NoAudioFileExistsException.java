package kr.co.okheeokey.audiofile.exception;

public class NoAudioFileExistsException extends RuntimeException {
    public NoAudioFileExistsException(String message) {
        super(message);
    }
}
