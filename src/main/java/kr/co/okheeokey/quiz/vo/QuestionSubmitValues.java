package kr.co.okheeokey.quiz.vo;

import lombok.Getter;

@Getter
public class QuestionSubmitValues {
    private Long quizId;
    private Long questionId;
    private Long responseSongId;

    public QuestionSubmitValues(Long quizId, Long questionId, Long responseSongId) {
        this.quizId = quizId;
        this.questionId = questionId;
        this.responseSongId = responseSongId;
    }
}
