package kr.co.okheeokey.audiofile.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class AudioFileSetValues {
    private final MultipartFile file;
    private final Long questionId;
    private final Long difficulty;
}
