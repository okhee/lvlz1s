package kr.co.okheeokey.question.vo;

import kr.co.okheeokey.song.vo.SongInfoValues;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionResultValues {
    private final SongInfoValues songInfoValues;
    private final Boolean correct;
    private final String youtubeAddress;

    @Builder
    public QuestionResultValues(SongInfoValues songInfoValues, Boolean correct, String youtubeAddress) {
        this.songInfoValues = songInfoValues;
        this.correct = correct;
        this.youtubeAddress = youtubeAddress;
    }
}
