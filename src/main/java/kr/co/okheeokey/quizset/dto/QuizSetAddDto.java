package kr.co.okheeokey.quizset.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizSetAddDto {
    private Long userId;
    private String title;
    private String description;
    private List<Long> songFileIdList;

    @JsonCreator
    public QuizSetAddDto(@JsonProperty("userId") Long userId,
                         @JsonProperty("title") String title,
                         @JsonProperty("description") String description,
                         @JsonProperty("songFileIdList") List<Long> songFileIdList) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.songFileIdList = songFileIdList;
    }
}
