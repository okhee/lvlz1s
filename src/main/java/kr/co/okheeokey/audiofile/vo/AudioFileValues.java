package kr.co.okheeokey.audiofile.vo;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

@Getter
public class AudioFileValues {
    private final InputStream audioStream;
    private final Long contentLength;
    private final String mimeType;

    @Builder
    public AudioFileValues(InputStream audioStream, Long contentLength, String mimeType) {
        this.audioStream = audioStream;
        this.contentLength = contentLength;
        this.mimeType = mimeType;
    }
}
