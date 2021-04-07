package kr.co.okheeokey.domain.quiz;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUIZ_ID")
    private Quiz quiz;

    private Long songId;

    private Long songFileId;

    @Builder
    public Question(Long songId, Long songFileId) {
        this.songId = songId;
        this.songFileId = songFileId;
    }

    public void setQuiz(Quiz quiz) {
        if(this.quiz != null) {
            this.quiz.getQuestions().remove(this);
        }
        this.quiz = quiz;
        quiz.getQuestions().add(this);
    }
}
