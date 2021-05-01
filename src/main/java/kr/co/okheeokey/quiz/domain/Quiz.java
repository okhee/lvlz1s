package kr.co.okheeokey.quiz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.song.Song;
import kr.co.okheeokey.song.SongFile;
import kr.co.okheeokey.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "QUIZ_SET_ID")
    private QuizSet quizSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "USER_ID")
    private User owner;

    @OneToMany
    @JsonManagedReference
    private List<SongFile> songList = new ArrayList<>();

    @OneToMany
    @JsonManagedReference
    private List<Song> responseList = new ArrayList<>();

    private Boolean closed;

    @Builder
    public Quiz(QuizSet quizSet, User owner, List<SongFile> songList, List<Song> responseList, Boolean closed) {
        this.quizSet = quizSet;
        this.owner = owner;
        this.songList = songList;
        this.responseList = responseList;
        this.closed = closed;
    }
}
