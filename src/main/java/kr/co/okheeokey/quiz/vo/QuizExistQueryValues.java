package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.user.domain.User;
import lombok.Getter;

@Getter
public class QuizExistQueryValues {
    private User user;
    private Long quizSetId;

    public QuizExistQueryValues(User user, Long quizSetId) {
        this.user = user;
        this.quizSetId = quizSetId;
    }
}
