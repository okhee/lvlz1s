package kr.co.okheeokey.web.dto;

import kr.co.okheeokey.domain.song.Song;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SongAddDto {
    private String songName;
    private Long albumId;
    private Long fileId;

    @Builder
    public SongAddDto(String songName, Long albumId, Long fileId) {
        this.songName = songName;
        this.albumId = albumId;
        this.fileId = fileId;
    }

    public Song toEntity() {
        return new Song(songName, albumId, fileId);
    }
}
