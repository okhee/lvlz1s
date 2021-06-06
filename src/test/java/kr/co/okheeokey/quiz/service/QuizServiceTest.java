package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quiz.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizExistQueryValues;
import kr.co.okheeokey.quiz.vo.QuizStatusValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.user.User;
import kr.co.okheeokey.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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
    @Mock private Song song;

    private final Long userId = 51L;
    private final Long quizSetId = 73L;
    private final Long songNum = 12L;
    private final Long quizId = 83L;
    private final Long questionId = songNum - 2L;
    private final Long responseSongId = 15L;

    @Test
    public void previousQuiz() throws Exception {
        // given
        QuizExistQueryValues values = new QuizExistQueryValues(userId, quizSetId);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getOwnerId()).thenReturn(userId);
        when(quizRepository.findByOwnerAndQuizSetAndClosed(any(User.class), any(QuizSet.class), anyBoolean()))
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

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getOwnerId()).thenReturn(userId + 1);

        // when
        quizService.previousQuiz(values);
    }

    @Test
    public void createNewQuiz() {
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
    public void createNewQuiz_withInvalidUser() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, songNum));
    }

    @Test(expected = NoSuchElementException.class)
    public void createNewQuiz_withInvalidQuizSet() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, songNum));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void createNewQuiz_withInvalidSongNum() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getSongPool()).thenReturn(Collections.emptyList());

        assert songNum > 0;

        // when
        quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, songNum));
    }

    @Test
    public void saveQuestionResponse() {
        // given
        Quiz quiz = new Quiz(quizSet, user, randomSongList, songNum, false);

        when(quizRepository.findByIdAndClosed(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(quiz));
        when(songRepository.findById(anyLong()))
                .thenReturn(Optional.of(song));
        when(song.getId()).thenReturn(responseSongId);

        assert questionId <= songNum;
        QuestionSubmitValues values = new QuestionSubmitValues(quizId, questionId, responseSongId);

        // when
        Quiz returnQuiz = quizService.saveQuestionResponse(values);

        // then
        assertEquals(quiz, returnQuiz);
        assertEquals(responseSongId, returnQuiz.getResponseMap().get(questionId - 1L));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void saveQuestionResponse_withInvalidQuestionId() {
        // given
        Long questionId = songNum + 2L;

        when(quizRepository.findByIdAndClosed(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(quiz));
        when(songRepository.findById(anyLong()))
                .thenReturn(Optional.of(song));
        when(quiz.getQuestionNum()).thenReturn(songNum);

        assert questionId > songNum;
        QuestionSubmitValues values = new QuestionSubmitValues(quizId, questionId, responseSongId);

        // when
        quizService.saveQuestionResponse(values);
    }

    @Test
    public void getQuizStatus() {
        // given
        String title = "180h12f";
        String description = "f1803xz";
        Boolean closed = (Math.random() < 0.5);
        long questionNum = Math.round(Math.random() * 5 + 7);
        Map<Long, Long> responseMap = new HashMap<>();
        for(int i = 0; i < questionNum; i++){
            if (Math.random() < 0.35)
                responseMap.put((long) i, 55L);
        }

        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(quiz.getQuizSet()).thenReturn(quizSet);
        when(quiz.getClosed()).thenReturn(closed);
        when(quiz.getQuestionNum()).thenReturn(questionNum);
        when(quiz.getResponseMap()).thenReturn(responseMap);

        when(quizSet.getTitle()).thenReturn(title);
        when(quizSet.getDescription()).thenReturn(description);

        // when
        QuizStatusValues values = quizService.getQuizStatus(quizId);

        // then
        assertThat(values.getTitle(), is(title));
        assertThat(values.getDescription(), is(description));
        assertThat(values.getClosed(), is(closed));
        assertThat(values.getQuestionNum(), is(questionNum));

        List<Boolean> responseList = values.getResponseExistList();
        for(int i = 0; i < questionNum; i++){
            assertEquals(responseList.get(i), responseMap.containsKey((long) i));
        }
    }
}
