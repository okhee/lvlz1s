package kr.co.okheeokey.song.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SongInfoValues {
    private String songName;
    private AlbumInfoValues albumValues;

    @Builder
    public SongInfoValues(String songName, AlbumInfoValues albumValues) {
        this.songName = songName;
        this.albumValues = albumValues;
    }
}
