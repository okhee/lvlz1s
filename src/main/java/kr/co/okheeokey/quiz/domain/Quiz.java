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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ElementCollection
    private Map<Long, Long> responseMap = new HashMap<>();

    @ElementCollection
    private Map<Long, Boolean> scoreList = new HashMap<>();

    private Boolean closed;

    @Builder
    public Quiz(QuizSet quizSet, User owner, List<SongFile> songList, Boolean closed) {
        this.quizSet = quizSet;
        this.owner = owner;
        this.songList = songList;
        this.closed = closed;
    }
//
//    public synchronized void initResponseList() {
//    }

    public void close() {
        this.closed = true;
    }

    public void saveResponse(Long questionIdx, Song response) {
        responseMap.put(questionIdx, response.getId());
    }

    public void scoreResponse() {
        for (Integer i = 0; i < this.songList.size(); i++){
            Boolean isAnswer = this.songList.get(i).getSong().getId().equals(this.responseMap.get(i.longValue()));
            this.scoreList.put(i.longValue(), isAnswer);
        }
    }

}
