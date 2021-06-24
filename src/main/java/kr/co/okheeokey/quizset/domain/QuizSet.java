package kr.co.okheeokey.quizset.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.question.domain.Question;
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

    // Todo: Long ownerId must be changed into 'User owner'
    private Long ownerId;

    private String title;

    private String description;

    @ManyToMany
    @JsonManagedReference
    private List<Question> questionPool;

    private Double averageDifficulty;

    @Builder
    public QuizSet(Long ownerId, String title, String description, List<Question> questionPool) {
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.questionPool = questionPool;
        this.averageDifficulty = questionPool.stream()
                .map(q -> q.getDifficulty().ordinal())
                .reduce(Integer::sum)
                .orElseThrow(IllegalArgumentException::new)
                .doubleValue() / questionPool.size();
    }

}
