package kr.co.okheeokey.quizset.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Entity
public class QuizSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "USER_ID")
    private User owner;

    private String title;

    private String description;

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
