package kr.co.okheeokey.song.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.question.domain.Question;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "songName", "album"})
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String songName;

    @OneToMany(mappedBy = "song")
    @JsonManagedReference
    private List<Question> question = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "ALBUM_ID")
    private Album album;

    @Builder
    public Song(String songName) {
        this.songName = songName;
    }

    public void setAlbum(Album album) {
        if(this.album != null) {
            this.album.getSongList().remove(this);
        }
        this.album = album;
        album.getSongList().add(this);
    }

}
