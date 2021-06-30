package kr.co.okheeokey.quizset.vo;

import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class QuizSetCreateValues {
    private final String title;
    private final String description;
    private final List<Long> albumIdList;
    private final List<Long> songIdList;
    private final Boolean easy;
    private final Boolean medium;
    private final Boolean hard;

    public QuizSetCreateValues(QuizSetAddDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.albumIdList = dto.getAlbumIdList();
        this.songIdList = dto.getSongIdList();
        this.easy = dto.getEasy();
        this.medium = dto.getMedium();
        this.hard = dto.getHard();
    }
}
