package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import lombok.Getter;

@Getter
public class QuizCreateValues {
    private Long userId;
    private Long quizSetId;
    private Long songNum;

    public QuizCreateValues(QuizCreateDto dto) {
        this.userId = dto.getUserId();
        this.quizSetId = dto.getQuizSetId();
        this.songNum = dto.getSongNum();
    }

    public QuizCreateValues(Long userId, Long quizSetId, Long songNum) {
        this.userId = userId;
        this.quizSetId = quizSetId;
        this.songNum = songNum;
    }
}
