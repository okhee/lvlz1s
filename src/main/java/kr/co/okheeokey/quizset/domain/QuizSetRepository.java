package kr.co.okheeokey.quizset.domain;

import kr.co.okheeokey.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {
    List<QuizSet> findAllByReadyMadeIsTrue();
    List<QuizSet> findAllByOwner(User user);
}
