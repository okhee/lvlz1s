package kr.co.okheeokey.song.vo;

import kr.co.okheeokey.song.domain.Album;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AlbumInfoValues {
    private final String albumName;
    private final LocalDate releasedDate;
    private final String albumCover;

    public AlbumInfoValues(Album album) {
        this.albumName = album.getAlbumName();
        this.releasedDate = album.getReleasedDate();
        this.albumCover = album.getAlbumCover();
    }
}
