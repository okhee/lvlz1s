package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongFile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class QuizResultValues {
    private Long quizId;
    private String quizSetTitle;
    private String quizSetDescription;
    private List<SongFile> songList;
    private Map<Long, Long> responseMap;
    private Map<Long, Boolean> scoreList;

    @Builder
    public QuizResultValues(Long quizId, String quizSetTitle, String quizSetDescription, List<SongFile> songList, Map<Long, Long> responseMap, Map<Long, Boolean> scoreList) {
        this.quizId = quizId;
        this.quizSetTitle = quizSetTitle;
        this.quizSetDescription = quizSetDescription;
        this.songList = songList;
        this.responseMap = responseMap;
        this.scoreList = scoreList;
    }
}
