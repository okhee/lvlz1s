package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.user.domain.User;
import lombok.Getter;

@Getter
public class QuestionSubmitValues {
    private final User user;
    private final Long quizId;
    private final Long questionId;
    private final Long responseSongId;

    public QuestionSubmitValues(User user, Long quizId, Long questionId, Long responseSongId) {
        this.user = user;
        this.quizId = quizId;
        this.questionId = questionId;
        this.responseSongId = responseSongId;
    }
}
