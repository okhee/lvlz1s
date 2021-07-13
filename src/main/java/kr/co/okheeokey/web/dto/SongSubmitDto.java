package kr.co.okheeokey.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SongSubmitDto {
    private Long songId;
    private Long nextSongId;

    @Builder
    public SongSubmitDto(Long songId, Long nextSongId) {
        this.songId = songId;
        this.nextSongId = nextSongId;
    }
}
