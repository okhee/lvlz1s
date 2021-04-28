package kr.co.okheeokey.quiz.vo;

import lombok.Getter;

@Getter
public class QuizCreateValues {
    private Long userId;
    private Long quizSetId;
    private Long songNum;

    public QuizCreateValues(Long userId, Long quizSetId, Long songNum) {
        this.userId = userId;
        this.quizSetId = quizSetId;
        this.songNum = songNum;
    }
}
