package kr.co.okheeokey.web.dto;

import kr.co.okheeokey.song.domain.Song;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SongAddDto {
    private String songName;

    @Builder
    public SongAddDto(String songName) {
        this.songName = songName;
    }

    public Song toEntity() {
        return new Song(songName);
    }
}
