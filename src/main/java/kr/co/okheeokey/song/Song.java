package kr.co.okheeokey.song;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Builder
    public Song(String songName) {
        this.songName = songName;
    }

    public List<SongHash> getSongHash() {
        return songHash;
    }

    public List<SongFile> getSongFile() {
        return songFile;
    }

    public void setAlbum(Album album) {
        if(this.album != null) {
            this.album.getSongList().remove(this);
        }
        this.album = album;
        album.getSongList().add(this);
    }

    public Boolean songNameMatch(String songName) {
        return this.songName.equals(songName);
    }
}
