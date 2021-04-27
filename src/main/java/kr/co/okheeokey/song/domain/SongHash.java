package kr.co.okheeokey.song.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class SongHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String songHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "SONG_ID")
    private Song song;

    @Builder
    public SongHash(String songHash) {
        this.songHash = songHash;
    }

    public String getSongHash() {
        return songHash;
    }

    public void setSong(Song song) {
        if(this.song != null){
            this.song.getSongHash().remove(this);
        }
        this.song = song;
        song.getSongHash().add(this);
    }
}
