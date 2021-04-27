package kr.co.okheeokey.quiz;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Getter
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long difficulty;

    @OneToMany(mappedBy = "quiz")
    private Set<Question> questions = new HashSet<>();

    @Builder
    public Quiz(Long difficulty) {
        this.difficulty = difficulty;
    }
}
