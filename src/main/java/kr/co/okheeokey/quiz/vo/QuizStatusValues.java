package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quizset.domain.QuizSet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuizStatusValues {
    private final String title;
    private final String description;
    private final Boolean closed;
    private final List<Long> responseExistList;
    private final Long questionNum;

    public QuizStatusValues(Quiz quiz, QuizSet quizSet) {
        this.title = quizSet.getTitle();
        this.description = quizSet.getDescription();
        this.closed = quiz.getClosed();
        this.responseExistList = quiz.getResponseMap().keySet().stream()
                .sorted().collect(Collectors.toCollection(ArrayList::new));
        this.questionNum = quiz.getQuestionNum();
    }
}
