package kr.co.okheeokey.quizset.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "quiz_set")
@SequenceGenerator(name = "QUIZ_SET_SEQ_GENERATOR",
                    sequenceName = "QUIZ_SET_SEQ",
                    initialValue = 1, allocationSize = 1)
public class QuizSet {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
                        generator = "QUIZ_SET_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "bit(1) DEFAULT '0'")
    private Boolean readyMade = false;

    @ManyToMany
    @JsonManagedReference
    private List<Question> questionPool;

    private Double averageDifficulty;

    @Builder
    public QuizSet(User owner, String title, String description, Boolean readyMade, List<Question> questionPool) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.readyMade = readyMade;
        this.questionPool = questionPool;
        this.averageDifficulty = questionPool.stream()
                .map(q -> q.getDifficulty().ordinal())
                .reduce(Integer::sum)
                .orElseThrow(IllegalArgumentException::new)
                .doubleValue() / questionPool.size();
    }

}
