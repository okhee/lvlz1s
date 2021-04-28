package kr.co.okheeokey.domain.quiz;

import kr.co.okheeokey.quiz.domain.QuizRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuizTest {
    @Autowired
    private QuizRepository quizRepository;

    public QuizTest() {
    }

    @Test
    public void setQuiz() {


    }
}