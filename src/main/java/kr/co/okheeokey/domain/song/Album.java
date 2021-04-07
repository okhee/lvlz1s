package kr.co.okheeokey.domain.song;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String albumName;

    private LocalDate releasedDate;

    private String albumCover;

    @OneToMany(mappedBy = "album")
    private List<Song> songList = new ArrayList<>();

    @Builder
    public Album(String albumName, LocalDate releasedDate, String albumCover) {
        this.albumName = albumName;
        this.releasedDate = releasedDate;
        this.albumCover = albumCover;
    }

    public List<Song> getSongList() {
        return songList;
    }
}
