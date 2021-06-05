package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizExistQueryValues;
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

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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

    private final Long userId = 51L;
    private final Long quizSetId = 73L;
    private final Long songNum = 12L;

    @Test
    public void previousQuiz() throws Exception {
        // given
        QuizExistQueryValues values = new QuizExistQueryValues(userId, quizSetId);

        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getOwnerId()).thenReturn(userId);
        when(quizRepository.findByIdAndQuizSetAndClosed(anyLong(), any(QuizSet.class), anyBoolean()))
                .thenReturn(Optional.of(quiz));

        // when
        Optional<Quiz> previousQuiz = quizService.previousQuiz(values);

        // then
        assertTrue(previousQuiz.isPresent());
        assertEquals(quiz, previousQuiz.get());
    }

    @Test(expected = IllegalAccessException.class)
    public void previousQuiz_withInvalidAuthority() throws Exception {
        // given
        QuizExistQueryValues values = new QuizExistQueryValues(userId, quizSetId);

        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getOwnerId()).thenReturn(userId + 1);

        // when
        quizService.previousQuiz(values);
    }

    @Test
    public void createNewQuiz() throws Exception {
        // given
        QuizCreateValues values = new QuizCreateValues(userId, quizSetId, songNum);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getSongPool()).thenReturn(songPool);
        when(songPool.subList(anyInt(), anyInt())).thenReturn(randomSongList);
        when(quizRepository.save(any(Quiz.class))).thenReturn(new Quiz(quizSet, user, randomSongList, songNum, false));

        // when
        Quiz newQuiz = quizService.createNewQuiz(values);

        // then
        assertNotNull(newQuiz);
        assertEquals(user, newQuiz.getOwner());
        assertEquals(songNum, newQuiz.getQuestionNum());
        assertEquals(quizSet, newQuiz.getQuizSet());
        assertArrayEquals(randomSongList.toArray(), newQuiz.getSongList().toArray());
    }

    @Test(expected = NoSuchElementException.class)
    public void createNewQuiz_withInvalidUser() throws Exception {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, songNum));
    }

    @Test(expected = NoSuchElementException.class)
    public void createNewQuiz_withInvalidQuizSet() throws Exception {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, songNum));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void createNewQuiz_withInvalidSongNum() throws Exception {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getSongPool()).thenReturn(Collections.emptyList());

        assert songNum > 0;

        // when
        quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, songNum));
    }


}