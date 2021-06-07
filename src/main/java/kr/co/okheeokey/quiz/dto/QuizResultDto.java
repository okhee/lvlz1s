package kr.co.okheeokey.quiz.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kr.co.okheeokey.quiz.vo.QuizResultValues;
import kr.co.okheeokey.question.domain.Question;

import java.util.List;
import java.util.Map;

public class QuizResultDto {
    private Long quizId;
    private String quizSetTitle;
    private String quizSetDescription;
    private List<Question> songList;
    private Map<Long, Long> responseMap;
    private Map<Long, Boolean> scoreList;

    @JsonCreator
    public QuizResultDto(Long quizId, String quizSetTitle, String quizSetDescription, List<Question> songList, Map<Long, Long> responseMap, Map<Long, Boolean> scoreList) {
        this.quizId = quizId;
        this.quizSetTitle = quizSetTitle;
        this.quizSetDescription = quizSetDescription;
        this.songList = songList;
        this.responseMap = responseMap;
        this.scoreList = scoreList;
    }

    public static QuizResultDto of(QuizResultValues values) {
        return new QuizResultDto(values.getQuizId(),
                                values.getQuizSetTitle(),
                                values.getQuizSetDescription(),
                                values.getSongList(),
                                values.getResponseMap(),
                                values.getScoreList());
    }
}
