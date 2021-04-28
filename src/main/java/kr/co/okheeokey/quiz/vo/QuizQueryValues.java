package kr.co.okheeokey.quiz.vo;

import lombok.Getter;

@Getter
public class QuizQueryValues {
    private Long userId;
    private Long quizSetId;

    public QuizQueryValues(Long userId, Long quizSetId) {
        this.userId = userId;
        this.quizSetId = quizSetId;
    }
}
