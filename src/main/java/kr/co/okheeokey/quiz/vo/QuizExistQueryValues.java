package kr.co.okheeokey.quiz.vo;

import lombok.Getter;

@Getter
public class QuizExistQueryValues {
    private Long userId;
    private Long quizSetId;

    public QuizExistQueryValues(Long userId, Long quizSetId) {
        this.userId = userId;
        this.quizSetId = quizSetId;
    }
}
