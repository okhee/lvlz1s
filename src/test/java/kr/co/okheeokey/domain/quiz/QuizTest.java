package kr.co.okheeokey.domain.quiz;

import kr.co.okheeokey.quiz.Question;
import kr.co.okheeokey.quiz.QuestionRepository;
import kr.co.okheeokey.quiz.Quiz;
import kr.co.okheeokey.quiz.QuizRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuizTest {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public QuizTest() {
    }

    @Test
    public void setQuiz() {
        Quiz quiz1 = Quiz.builder()
                        .difficulty(102L)
                        .build();

        Question question1 = Question.builder()
                .songId(2151L)
                .songFileId(1242L)
                .build();

        question1.setQuiz(quiz1);

        Set<Question> questions = quiz1.getQuestions();

        quizRepository.save(quiz1);
        questionRepository.save(question1);

    }
}