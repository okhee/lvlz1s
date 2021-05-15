package kr.co.okheeokey.quiz.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class QuestionSubmitDto {
    private Long responseSongId;

    @JsonCreator
    public QuestionSubmitDto(@JsonProperty("responseSongId") Long responseSongId) {
        this.responseSongId = responseSongId;
    }
}
