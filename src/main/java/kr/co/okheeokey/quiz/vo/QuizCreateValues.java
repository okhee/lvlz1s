package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import lombok.Getter;

@Getter
public class QuizCreateValues {
    private final Long userId;
    private final Long quizSetId;
    private final Long questionNum;

    public QuizCreateValues(QuizCreateDto dto) {
        this.userId = dto.getUserId();
        this.quizSetId = dto.getQuizSetId();
        this.questionNum = dto.getQuestionNum();
    }

    public QuizCreateValues(Long userId, Long quizSetId, Long questionNum) {
        this.userId = userId;
        this.quizSetId = quizSetId;
        this.questionNum = questionNum;
    }
}
