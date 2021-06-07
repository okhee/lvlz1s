package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.question.domain.Question;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class QuizResultValues {
    private final Long quizId;
    private final String quizSetTitle;
    private final String quizSetDescription;
    private final List<Question> songList;
    private final Map<Long, Long> responseMap;
    private final Map<Long, Boolean> scoreList;

    public QuizResultValues(Quiz quiz, QuizSet quizSet) {
        this.quizId = quiz.getId();
        this.quizSetTitle = quizSet.getTitle();
        this.quizSetDescription = quizSet.getDescription();
        this.songList = quiz.getSongList();
        this.responseMap = quiz.getResponseMap();
        this.scoreList = quiz.getScoreList();
    }
}
