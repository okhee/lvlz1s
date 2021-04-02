package kr.co.okheeokey.domain.song;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String songName;

    private Long albumId;

    private Long fileId;

    @Builder
    public Song(String songName, Long albumId, Long fileId) {
        this.songName = songName;
        this.albumId = albumId;
        this.fileId = fileId;
    }

    public Boolean songNameMatch(String songName) {
        return this.songName.equals(songName);
    }
}
