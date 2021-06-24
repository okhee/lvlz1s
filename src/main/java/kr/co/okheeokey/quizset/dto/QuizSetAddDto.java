package kr.co.okheeokey.quizset.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizSetAddDto {
    private final Long userId;
    private final String title;
    private final String description;
    private final List<Long> albumIdList;
    private final List<Long> songIdList;
    private final Boolean easy;
    private final Boolean medium;
    private final Boolean hard;

    @JsonCreator
    public QuizSetAddDto(@JsonProperty("userId") Long userId,
                         @JsonProperty("title") String title,
                         @JsonProperty("description") String description,
                         @JsonProperty("albumIdList") List<Long> albumIdList,
                         @JsonProperty("songIdList") List<Long> songIdList,
                         @JsonProperty("easy") Boolean easy,
                         @JsonProperty("medium") Boolean medium,
                         @JsonProperty("hard") Boolean hard) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.albumIdList = albumIdList;
        this.songIdList = songIdList;

        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
    }
}
