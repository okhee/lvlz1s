package kr.co.okheeokey.quiz.domain;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByIdAndClosed(Long id, Boolean closed);
    Optional<Quiz> findByOwnerAndQuizSetAndClosed(User user, QuizSet quizSet, Boolean closed);
}
