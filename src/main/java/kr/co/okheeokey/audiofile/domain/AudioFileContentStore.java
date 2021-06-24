package kr.co.okheeokey.audiofile.domain;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Component;

@Component
public interface AudioFileContentStore extends ContentStore<AudioFile, String> {
}
