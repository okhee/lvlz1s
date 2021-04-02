package kr.co.okheeokey.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SongSubmitDto {
    private String songHash;
    private Long songId;
    private Long nextSongId;

    @Builder
    public SongSubmitDto(String songHash, Long songId, Long nextSongId) {
        this.songHash = songHash;
        this.songId = songId;
        this.nextSongId = nextSongId;
    }
}
