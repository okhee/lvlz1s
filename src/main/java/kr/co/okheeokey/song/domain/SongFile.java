package kr.co.okheeokey.song.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class SongFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "SONG_ID")
    private Song song;

    @Column(nullable = false)
    private String songFileName;

    @Builder
    public SongFile(String songFileName) {
        this.songFileName = songFileName;
    }

    public void setSong(Song song) {
        if(this.song != null){
            this.song.getSongFile().remove(this);
        }
        this.song = song;
        song.getSongFile().add(this);
    }

    @Override
    public String toString() {
        return "SongFile{" +
                "id=" + id +
                ", song=" + song +
                ", songFileName='" + songFileName + '\'' +
                '}';
    }
}

