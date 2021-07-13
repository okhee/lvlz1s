package kr.co.okheeokey.song.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "album")
public class Album {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String albumName;

    @Column(nullable = false)
    private LocalDate releasedDate;

    @Column(nullable = false)
    private String albumCover;

    @OneToMany(mappedBy = "album")
    @JsonManagedReference
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
