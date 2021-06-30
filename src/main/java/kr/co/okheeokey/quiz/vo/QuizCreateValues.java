package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.user.domain.User;
import lombok.Getter;

@Getter
public class QuizCreateValues {
    private final User user;
    private final Long quizSetId;
    private final Long questionNum;

    public QuizCreateValues(User user, QuizCreateDto dto) {
        this.user = user;
        this.quizSetId = dto.getQuizSetId();
        this.questionNum = dto.getQuestionNum();
    }

    public QuizCreateValues(User user, Long quizSetId, Long questionNum) {
        this.user = user;
        this.quizSetId = quizSetId;
        this.questionNum = questionNum;
    }
}
