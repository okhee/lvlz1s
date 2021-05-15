package kr.co.okheeokey.quiz.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class QuizCreateDto {
    private Long userId;
    private Long quizSetId;
    private Long songNum;

    @JsonCreator
    public QuizCreateDto(@JsonProperty("userId") Long userId,
                         @JsonProperty("quizSetId") Long quizSetId,
                         @JsonProperty("songNum") Long songNum) {
        this.userId = userId;
        this.quizSetId = quizSetId;
        this.songNum = songNum;
    }
}
