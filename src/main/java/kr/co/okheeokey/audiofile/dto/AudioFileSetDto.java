package kr.co.okheeokey.audiofile.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AudioFileSetDto {
    private final Long questionId;
    private final Long difficulty;

    @JsonCreator
    public AudioFileSetDto(@JsonProperty("questionId") Long questionId,
                           @JsonProperty("difficulty") Long difficulty) {
        this.questionId = questionId;
        this.difficulty = difficulty;
    }
}
