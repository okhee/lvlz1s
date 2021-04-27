package kr.co.okheeokey.quizset.vo;

import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizSetCreateValues {
    private Long ownerId;
    private List<Long> songFileIdList;
    private String title;
    private String description;

    public QuizSetCreateValues(QuizSetAddDto quizSetAddDto) {
        ownerId = quizSetAddDto.getUserId();
        songFileIdList = quizSetAddDto.getSongFileIdList();
        title = quizSetAddDto.getTitle();
        description = quizSetAddDto.getDescription();
    }
}
