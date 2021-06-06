package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.songfile.domain.SongFileRepository;
import kr.co.okheeokey.user.User;
import kr.co.okheeokey.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    private SongFileRepository songFileRepository;

    @Test
    // Testing whether two new quiz instance can be created with identical information.
    // In QuizController, however, createQuiz method ensures that
    // only one ongoing quiz can exist at the same time with same (User, QuizSet) information.
    public void createDuplicatedQuiz() {
        // given
        SongFile songFile = songFileRepository.save(new SongFile("soifnaeo"));
        List<SongFile> songPool = Collections.singletonList(songFile);

        User user = userRepository.save(User.builder()
                .name("eahr")
                .isAlive(true)
                .build());
        QuizSet quizSet = quizSetRepository.save(QuizSet.builder()
                .ownerId(user.getId())
                .songPool(songPool)
                .title("ttiteafew")
                .description("dfoeiw")
                .build());

        // when
        Quiz quiz1 = quizService.createNewQuiz(new QuizCreateValues(user.getId(), quizSet.getId(), 0L));
        Quiz quiz2 = quizService.createNewQuiz(new QuizCreateValues(user.getId(), quizSet.getId(), 0L));

        // then
        assertNotEquals(quiz1.getId(), quiz2.getId());
        assertThat(quiz1.getOwner(), is(quiz2.getOwner()));
        assertThat(quiz1.getQuizSet(), is(quiz2.getQuizSet()));
        assertThat(quiz1.getQuestionNum(), is(quiz2.getQuestionNum()));
    }



}
