package kr.co.okheeokey.quiz;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QuizTest {
    @Autowired
    public QuizRepository quizRepository;

    @Test
    public void testQuizTimeStamp() {
        LocalDateTime now = LocalDateTime.now();

        Quiz quiz = new Quiz();

        quizRepository.save(quiz);

        assertThat(quiz.getCreatedDate()).isAfter(now);
        assertThat(quiz.getLastModifiedDate()).isAfter(now);
    }
}
