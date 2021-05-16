package kr.co.okheeokey.song.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.songfile.domain.SongFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String songName;

    @OneToMany(mappedBy = "song")
    @JsonManagedReference
    private List<SongHash> songHash = new ArrayList<>();

    @OneToMany(mappedBy = "song")
    @JsonManagedReference
    private List<SongFile> songFile = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "ALBUM_ID")
    private Album album;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id.equals(song.id) && songName.equals(song.songName) && album.equals(song.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, songName, album);
    }

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
