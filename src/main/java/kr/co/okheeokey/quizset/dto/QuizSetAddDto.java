package kr.co.okheeokey.quizset.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizSetAddDto {
    private String title;
    private String description;
    private List<Long> songFileIdList;

    @Builder
    public QuizSetAddDto(String title, String description, List<Long> songFileIdList) {
        this.title = title;
        this.description = description;
        this.songFileIdList = songFileIdList;
    }
}
