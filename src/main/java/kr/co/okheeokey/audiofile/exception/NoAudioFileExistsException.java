package kr.co.okheeokey.audiofile.exception;

public class NoAudioFileExistsException extends RuntimeException {
    public NoAudioFileExistsException(String message) {
        super(message);
    }

    public NoAudioFileExistsException(Long questionId, Long difficulty) {
        super("Audio file does not exists in Question id { " + questionId
                + " }, difficulty { " + difficulty + " }; Add audio file first");
    }
}
