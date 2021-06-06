package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quizset.domain.QuizSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public class QuizStatusValues {
    private final String title;
    private final String description;
    private final Boolean closed;
    private final List<Boolean> responseExistList;
    private final Long questionNum;

    public QuizStatusValues(Quiz quiz, QuizSet quizSet) {
        this.title = quizSet.getTitle();
        this.description = quizSet.getDescription();
        this.closed = quiz.getClosed();
        this.questionNum = quiz.getQuestionNum();
        this.responseExistList = new ArrayList<>();
        IntStream.range(0, this.questionNum.intValue())
                .forEach(i -> this.responseExistList.add(false));
        quiz.getResponseMap().keySet()
                .forEach(i -> this.responseExistList.set(i.intValue(), true));
    }
}
