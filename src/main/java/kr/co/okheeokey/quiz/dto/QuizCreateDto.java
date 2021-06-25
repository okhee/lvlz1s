package kr.co.okheeokey.quiz.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class QuizCreateDto {
    private final Long userId;
    private final Long quizSetId;
    private final Long questionNum;

    @JsonCreator
    public QuizCreateDto(@JsonProperty("userId") Long userId,
                         @JsonProperty("quizSetId") Long quizSetId,
                         @JsonProperty("questionNum") Long questionNum) {
        this.userId = userId;
        this.quizSetId = quizSetId;
        this.questionNum = questionNum;
    }
}
