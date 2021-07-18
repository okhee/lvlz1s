package kr.co.okheeokey.question.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByDifficulty(QuestionDifficulty difficulty);
}
