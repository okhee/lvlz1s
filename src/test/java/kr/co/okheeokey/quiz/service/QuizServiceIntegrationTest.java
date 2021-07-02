package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class QuizServiceIntegrationTest {
    @Autowired
    private QuizService quizService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizSetRepository quizSetRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    // Testing whether two new quiz instance can be created with identical information.
    // In QuizController, however, createQuiz method ensures that
    // only one ongoing quiz can exist at the same time with same (User, QuizSet) information.
    public void createDuplicatedQuiz() {
        // given
        Question question = questionRepository.save(new Question("soifnaeo"));
        List<Question> questionPool = Collections.singletonList(question);

        User user = userRepository.save(new User("naname", "pass", null));
        QuizSet quizSet = quizSetRepository.save(QuizSet.builder()
                .owner(user)
                .questionPool(questionPool)
                .title("ttiteafew")
                .description("dfoeiw")
                .build());

        // when
        Quiz quiz1 = quizService.createNewQuiz(new QuizCreateValues(user, quizSet.getId(), 0L));
        Quiz quiz2 = quizService.createNewQuiz(new QuizCreateValues(user, quizSet.getId(), 0L));

        // then
        assertNotEquals(quiz1.getId(), quiz2.getId());
        assertThat(quiz1.getOwner(), is(quiz2.getOwner()));
        assertThat(quiz1.getQuizSet(), is(quiz2.getQuizSet()));
        assertThat(quiz1.getQuestionNum(), is(quiz2.getQuestionNum()));
    }



}
