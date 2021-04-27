package kr.co.okheeokey.quizset.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.song.SongFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class QuizSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    @OneToMany
    @JsonManagedReference
    private List<SongFile> songPool = new ArrayList<>();

    private String title;

    private String description;

    @Builder
    public QuizSet(Long ownerId, List<SongFile> songPool, String title, String description) {
        this.ownerId = ownerId;
        this.songPool = songPool;
        this.title = title;
        this.description = description;
    }
}
