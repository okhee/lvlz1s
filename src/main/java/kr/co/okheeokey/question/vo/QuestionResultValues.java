package kr.co.okheeokey.question.vo;

import kr.co.okheeokey.song.vo.SongInfoValues;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionResultValues {
    private final SongInfoValues songInfoValues;
    private final Boolean correct;
    private final String youtubeAddress;
    private final Long answerLocationInSecond;

    @Builder
    public QuestionResultValues(SongInfoValues songInfoValues, Boolean correct, String youtubeAddress, Long answerLocationInSecond) {
        this.songInfoValues = songInfoValues;
        this.correct = correct;
        this.youtubeAddress = youtubeAddress;
        this.answerLocationInSecond = answerLocationInSecond;
    }
}
