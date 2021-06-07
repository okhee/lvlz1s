package kr.co.okheeokey.quiz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @ManyToMany
    @JsonManagedReference
    private List<Question> songList = new ArrayList<>();

    @ElementCollection
    private final Map<Long, Long> responseMap = new HashMap<>();

    @ElementCollection
    private final Map<Long, Boolean> scoreList = new HashMap<>();

    private Long questionNum;

    private Boolean closed;

    @Builder
    public Quiz(QuizSet quizSet, User owner, List<Question> songList, Long questionNum, Boolean closed) {
        this.quizSet = quizSet;
        this.owner = owner;
        this.songList = songList;
        this.questionNum = questionNum;
        this.closed = closed;
    }

    public void close() {
        this.closed = true;
    }

    public void saveResponse(Long questionIdx, Song response) {
        responseMap.put(questionIdx, response.getId());
    }

    public void scoreResponse() {
        for (int i = 0; i < this.songList.size(); i++){
            Boolean isAnswer = this.songList.get(i).getSong().getId().equals(this.responseMap.get((long) i));
            this.scoreList.put((long) i, isAnswer);
        }
    }

}
