package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.user.User;
import kr.co.okheeokey.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class QuizServiceTest {
    @InjectMocks
    private QuizService quizService;

    @Mock private SongRepository songRepository;
    @Mock private UserRepository userRepository;
    @Mock private QuizRepository quizRepository;
    @Mock private QuizSetRepository quizSetRepository;

    @Mock private Quiz quiz;
    @Mock private User user;
    @Mock private QuizSet quizSet;
    @Mock private List<SongFile> songPool;
    @Mock private List<SongFile> randomSongList;

    @Test
    public void createNewQuiz() throws Exception {
        // given
        Long userId = 51L;
        Long quizSetId = 73L;
        Long songNum = 12L;

        QuizCreateValues values = new QuizCreateValues(userId, quizSetId, songNum);

        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.of(quizSet)).when(quizSetRepository).findById(anyLong());
        doReturn(songPool).when(quizSet).getSongPool();
        doReturn(randomSongList).when(songPool).subList(anyInt(), anyInt());
        doReturn(new Quiz(quizSet, user, randomSongList, songNum, false))
                .when(quizRepository).save(any(Quiz.class));

        // when
        Quiz newQuiz = quizService.createNewQuiz(values);

        // then
        assertNotNull(newQuiz);
        assertEquals(user, newQuiz.getOwner());
        assertEquals(songNum, newQuiz.getQuestionNum());
        assertEquals(quizSet, newQuiz.getQuizSet());
        assertArrayEquals(randomSongList.toArray(), newQuiz.getSongList().toArray());
    }
}