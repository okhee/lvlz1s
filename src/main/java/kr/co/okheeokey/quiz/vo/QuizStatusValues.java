package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quizset.domain.QuizSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public class QuizStatusValues {
    private final String title;
    private final String description;
    private final Boolean closed;
    private final Long questionNum;

    private final List<QuestionDifficulty> questionList;
    private final List<Long> responseList;
    private final List<Boolean> scoreList;

    public QuizStatusValues(Quiz quiz, QuizSet quizSet) {
        this.title = quizSet.getTitle();
        this.description = quizSet.getDescription();
        this.closed = quiz.getClosed();
        this.questionNum = quiz.getQuestionNum();

        this.questionList = quiz.getQuestionList().stream().map(Question::getDifficulty).collect(Collectors.toList());

        this.responseList = new ArrayList<>();
        IntStream.range(0, this.questionNum.intValue())
                .forEach(i -> this.responseList.add(-1L));
        quiz.getResponseMap()
                .forEach((key, value) -> this.responseList.set(key.intValue(), value));

        this.scoreList = new ArrayList<>();
        IntStream.range(0, this.questionNum.intValue())
                .forEach(i -> this.scoreList.add(false));
        if (closed) {
            quiz.getScoreList().forEach((key, value) -> this.scoreList.set(key.intValue(), value));
        }
    }
}
