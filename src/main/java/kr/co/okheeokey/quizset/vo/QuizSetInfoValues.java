package kr.co.okheeokey.quizset.vo;

import kr.co.okheeokey.quizset.domain.QuizSet;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizSetInfoValues {
    private final Long id;
    private final String title;
    private final String description;
    private final Boolean readyMade;
    private final Long questionPoolSize;
    private final Double averageDifficulty;

    @Builder
    public QuizSetInfoValues(Long id, String title, String description, Boolean readyMade, Long questionPoolSize, Double averageDifficulty) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.readyMade = readyMade;
        this.questionPoolSize = questionPoolSize;
        this.averageDifficulty = averageDifficulty;
    }

    public QuizSetInfoValues(QuizSet quizSet) {
        this.id = quizSet.getId();
        this.title = quizSet.getTitle();
        this.description = quizSet.getDescription();
        this.readyMade = quizSet.getReadyMade();
        this.questionPoolSize = (long) quizSet.getQuestionPool().size();
        this.averageDifficulty = quizSet.getAverageDifficulty();
    }
}
