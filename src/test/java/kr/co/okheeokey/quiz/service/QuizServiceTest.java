package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.question.vo.QuestionInfoValues;
import kr.co.okheeokey.question.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizStatusValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.util.CryptoUtils;
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
    @Mock private QuizRepository quizRepository;
    @Mock private QuizSetRepository quizSetRepository;

    @Mock private Quiz quiz;
    @Mock private User user;
    @Mock private QuizSet quizSet;
    @Mock private List<Question> questionPool;
    @Mock private List<Question> randomQuestionList;
    @Mock private Song song;
    @Mock private AudioFile audioFile;

    @Mock private Question question1;
    @Mock private Question question2;

    private final Long quizSetId = 73L;
    private final Long questionNum = 12L;
    private final Long quizId = 83L;
    private final Long questionIndex = questionNum - 2L;
    private final Long responseSongId = 15L;

    @Test
    public void createNewQuiz() throws Exception {
        // given
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getReadyMade()).thenReturn(false);
        when(quizSet.getOwner()).thenReturn(user);
        when(quizRepository.findByOwnerAndQuizSetAndClosed(any(User.class), any(QuizSet.class), anyBoolean()))
                .thenReturn(Optional.empty());
        when(quizSet.getQuestionPool()).thenReturn(questionPool);

        randomQuestionList = new ArrayList<>();
        randomQuestionList.add(new Question());
        when(questionPool.subList(anyInt(), anyInt())).thenReturn(randomQuestionList);
        when(quizRepository.save(any(Quiz.class))).thenReturn(new Quiz(quizSet, user, randomQuestionList, questionNum, false));

        QuizCreateValues values = new QuizCreateValues(user, quizSetId, questionNum);

        // when
        Quiz newQuiz = quizService.createNewQuiz(values);

        // then
        assertNotNull(newQuiz);
        assertEquals(user, newQuiz.getOwner());
        assertEquals(questionNum, newQuiz.getQuestionNum());
        assertEquals(quizSet, newQuiz.getQuizSet());
        assertArrayEquals(randomQuestionList.toArray(), newQuiz.getQuestionList().toArray());
    }

    @Test
    public void createNewQuiz_ifPreviousQuizExists() throws Exception {
        // given
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getReadyMade()).thenReturn(false);
        when(quizSet.getOwner()).thenReturn(user);
        when(quizSet.getQuestionPool()).thenReturn(questionPool);
        when(questionPool.subList(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(quizRepository.findByOwnerAndQuizSetAndClosed(any(User.class), any(QuizSet.class), anyBoolean()))
                .thenReturn(Optional.of(quiz));

        // when
        Quiz resultQuiz = quizService.createNewQuiz(new QuizCreateValues(user, quizSetId, questionNum));

        // then
        assertEquals(quiz, resultQuiz);
    }

    @Test
    public void createNewQuiz_readyMadeQuizSet() throws Exception {
        // given
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getReadyMade()).thenReturn(true);
        when(quizRepository.findByOwnerAndQuizSetAndClosed(any(User.class), any(QuizSet.class), anyBoolean()))
                .thenReturn(Optional.empty());
        when(quizSet.getQuestionPool()).thenReturn(questionPool);
        when(questionPool.subList(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(quizRepository.save(any())).thenReturn(quiz);

        // when
        Quiz resultQuiz = quizService.createNewQuiz(new QuizCreateValues(user, quizSetId, questionNum));

        // then
        assertEquals(quiz, resultQuiz);
    }

    @Test(expected = IllegalAccessException.class)
    public void createNewQuiz_withInvalidAuthority() throws Exception {
        // given
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getReadyMade()).thenReturn(false);
        when(quizSet.getOwner()).thenReturn(null);

        // when
        quizService.createNewQuiz(new QuizCreateValues(user, quizSetId, questionNum));
    }

    @Test(expected = NoSuchElementException.class)
    public void createNewQuiz_withInvalidQuizSet() throws Exception {
        // given
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        quizService.createNewQuiz(new QuizCreateValues(user, quizSetId, questionNum));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void createNewQuiz_withInvalidQuestionNum() throws Exception {
        // given
        when(quizSetRepository.findById(anyLong())).thenReturn(Optional.of(quizSet));
        when(quizSet.getReadyMade()).thenReturn(true);
        when(quizSet.getQuestionPool()).thenReturn(Collections.emptyList());
        when(quizRepository.findByOwnerAndQuizSetAndClosed(any(User.class), any(QuizSet.class), anyBoolean()))
                .thenReturn(Optional.of(quiz));

        assert questionNum > 0;

        // when
        quizService.createNewQuiz(new QuizCreateValues(user, quizSetId, questionNum));
    }

    @Test
    public void getQuestion() throws Exception {
        // given
        List<Question> randomQuestionList = new ArrayList<>();

        randomQuestionList.add(new Question());
        randomQuestionList.get(0).appendAudio(0L, audioFile);
        randomQuestionList.get(0).appendAudio(1L, audioFile);

        randomQuestionList.add(new Question());
        randomQuestionList.get(1).appendAudio(0L, audioFile);

        UUID uuid = UUID.randomUUID();
        when(audioFile.getUuid()).thenReturn(uuid);

        Quiz quiz = Quiz.builder()
                    .quizSet(quizSet)
                    .owner(user)
                    .questionList(randomQuestionList)
                    .questionNum((long) randomQuestionList.size())
                    .closed(false)
                    .build();

        when(quizRepository.findByIdAndClosed(anyLong(), anyBoolean()))
                .thenReturn(Optional.ofNullable(quiz));

        QuestionInfoValues values;

        // when 1
        values = quizService.getQuestion(user, quizId, 1L);
        // then 1
        assertEquals(uuid, CryptoUtils.decryptUuid(values.getEncryptUuid()));
        assertTrue(values.getHintAvailable());
        assertEquals(quiz.hintCost(1L), values.getNextHintCost());

        // when 2
        values = quizService.getQuestion(user, quizId, 2L);
        // then 2
        assertFalse(values.getHintAvailable());
        assertEquals(-1L, (long) values.getNextHintCost());
    }

    @Test
    public void getHint() throws Exception {
        // given
        List<Question> randomQuestionList = new ArrayList<>();
        AudioFile audioFile1 = new AudioFile(); audioFile1.setUuid(UUID.randomUUID());
        AudioFile audioFile2 = new AudioFile(); audioFile2.setUuid(UUID.randomUUID());
        AudioFile audioFile3 = new AudioFile(); audioFile3.setUuid(UUID.randomUUID());

        randomQuestionList.add(new Question());
        randomQuestionList.get(0).appendAudio(0L, audioFile1);
        randomQuestionList.get(0).appendAudio(1L, audioFile2);

        randomQuestionList.add(new Question());
        randomQuestionList.get(1).appendAudio(0L, audioFile3);
        Quiz quiz = Quiz.builder()
                .quizSet(quizSet)
                .owner(user)
                .questionList(randomQuestionList)
                .questionNum((long) randomQuestionList.size())
                .closed(false)
                .build();

        when(quizRepository.findByIdAndClosed(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(quiz));

        // Case 1: if additional hint is available, update and present new audio file
        // when 1
        assertEquals(audioFile1.getUuid(),
                CryptoUtils.decryptUuid(quizService.getQuestion(user, quizId, 1L).getEncryptUuid()));

        quizService.getAccessToNewHint(user, quizId, 1L);

        // then 1
        assertEquals(audioFile2.getUuid(),
                CryptoUtils.decryptUuid(quizService.getQuestion(user, quizId, 1L).getEncryptUuid()));

        // Case 2: if additional hint is not available, nothing happens
        // when 2
        assertEquals(audioFile3.getUuid(),
                CryptoUtils.decryptUuid(quizService.getQuestion(user, quizId, 2L).getEncryptUuid()));

        quizService.getAccessToNewHint(user, quizId, 2L);

        // then 2
        assertEquals(audioFile3.getUuid(),
                CryptoUtils.decryptUuid(quizService.getQuestion(user, quizId, 2L).getEncryptUuid()));
    }

    @Test
    public void saveQuestionResponse() throws IllegalAccessException {
        // given
        quiz = Quiz.builder()
                .quizSet(quizSet)
                .owner(user)
                .questionList(randomQuestionList)
                .questionNum(questionNum)
                .closed(false)
                .build();

        when(quizRepository.findByIdAndClosed(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(quiz));
        when(songRepository.findById(anyLong()))
                .thenReturn(Optional.of(song));
        when(song.getId()).thenReturn(responseSongId);

        assert questionIndex <= questionNum;
        QuestionSubmitValues values = new QuestionSubmitValues(user, quizId, questionIndex, responseSongId);

        // when
        Quiz returnQuiz = quizService.saveQuestionResponse(values);

        // then
        assertEquals(quiz, returnQuiz);
        assertEquals(responseSongId, returnQuiz.getResponseMap().get(questionIndex - 1L));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void saveQuestionResponse_withInvalidQuestionIndex() throws IllegalAccessException {
        // given
        Long questionIndex = questionNum + 2L;

        when(quizRepository.findByIdAndClosed(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(quiz));
        when(songRepository.findById(anyLong()))
                .thenReturn(Optional.of(song));
        when(quiz.getOwner()).thenReturn(user);
        when(quiz.getQuestionNum()).thenReturn(questionNum);

        assert questionIndex > questionNum;
        QuestionSubmitValues values = new QuestionSubmitValues(user, quizId, questionIndex, responseSongId);

        // when
        quizService.saveQuestionResponse(values);
    }

    @Test
    public void getQuizStatus() throws IllegalAccessException {
        // given
        String title = "180h12f";
        String description = "f1803xz";
        Boolean closed = (Math.random() < 0.5);
        long questionNum = Math.round(Math.random() * 5 + 7);

        List<Question> questionList = new ArrayList<>();
        for(int i = 0; i < questionNum; i++){
            if (i % 2 == 0)
                questionList.add(question1);
            else
                questionList.add(question2);
        }

        Map<Long, Long> responseMap = new HashMap<>();
        for(int i = 0; i < questionNum; i++){
            if (Math.random() < 0.35)
                responseMap.put((long) i, 55L);
        }

        Map<Long, Boolean> scoreList = Collections.singletonMap(questionIndex / 2, true);

        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(quiz.getOwner()).thenReturn(user);

        when(quiz.getQuizSet()).thenReturn(quizSet);

        when(quizSet.getTitle()).thenReturn(title);
        when(quizSet.getDescription()).thenReturn(description);

        when(quiz.getClosed()).thenReturn(closed);
        when(quiz.getQuestionNum()).thenReturn(questionNum);
        when(quiz.getQuestionList()).thenReturn(questionList);
        when(question1.getDifficulty()).thenReturn(QuestionDifficulty.MEDIUM);
        when(question2.getDifficulty()).thenReturn(QuestionDifficulty.HARD);

        when(quiz.getResponseMap()).thenReturn(responseMap);
        when(quiz.getScoreList()).thenReturn(scoreList);

        // when
        QuizStatusValues values = quizService.getQuizStatus(user, quizId);

        // then
        assertThat(values.getTitle(), is(title));
        assertThat(values.getDescription(), is(description));
        assertThat(values.getClosed(), is(closed));
        assertThat(values.getQuestionNum(), is(questionNum));

        assertThat(values.getQuestionList().size(), is(Math.toIntExact(questionNum)));
        assertArrayEquals(values.getQuestionList().toArray(),
                questionList.stream().map(Question::getDifficulty).toArray());

        for(int i = 0; i < questionNum; i++){
            if (responseMap.containsKey((long) i))
                assertThat(55L, is(values.getResponseList().get(i)));
            else
                assertThat(-1L, is(values.getResponseList().get(i)));
        }

        if (closed)
            assertThat(values.getScoreList().get(questionIndex.intValue() / 2), is(true));
        else
            assertFalse(values.getScoreList().stream()
                    .reduce(Boolean::logicalOr).get());
    }
}
