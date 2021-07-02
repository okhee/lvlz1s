package kr.co.okheeokey.audiofile.exception;

public class AudioFileAlreadyExistsException extends RuntimeException {
    public AudioFileAlreadyExistsException(String message) {
        super(message);
    }
    public AudioFileAlreadyExistsException(Long questionId, Long difficulty) {
        super("Audio file already exists in Question id { " + questionId
                + " }, difficulty { " + difficulty + " }; Use update request");
    }
}
