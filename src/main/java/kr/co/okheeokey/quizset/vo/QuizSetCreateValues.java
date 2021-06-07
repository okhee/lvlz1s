package kr.co.okheeokey.quizset.vo;

import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizSetCreateValues {
    private final Long ownerId;
    private final List<Long> questionIdList;
    private final String title;
    private final String description;

    public QuizSetCreateValues(QuizSetAddDto quizSetAddDto) {
        ownerId = quizSetAddDto.getUserId();
        questionIdList = quizSetAddDto.getQuestionIdList();
        title = quizSetAddDto.getTitle();
        description = quizSetAddDto.getDescription();
    }
}
