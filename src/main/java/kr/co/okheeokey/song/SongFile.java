package kr.co.okheeokey.song;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class SongFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SONG_ID")
    private Song song;

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
