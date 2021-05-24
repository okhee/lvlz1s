package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quiz.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizExistQueryValues;
import kr.co.okheeokey.quiz.vo.QuizResultValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.user.User;
import kr.co.okheeokey.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizSetRepository quizSetRepository;

    public Quiz createNewQuiz(QuizCreateValues values) {
        User user = userRepository.findById(values.getUserId()).orElseThrow(IllegalArgumentException::new);
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId()).orElseThrow(IllegalArgumentException::new);
        List<SongFile> randomSongList = sampleSongList(quizSet.getSongPool(), values.getSongNum());

        return quizRepository.save(
                Quiz.builder()
                        .quizSet(quizSet)
                        .owner(user)
                        .songList(randomSongList)
                        .closed(false)
                        .build()
        );
    }

    public SongFile getQuestion(Long quizId, Long questionId) throws IndexOutOfBoundsException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false).orElseThrow(NoSuchElementException::new);

        if (questionId >= quiz.getSongList().size())
            throw new IndexOutOfBoundsException();
        return quiz.getSongList().get(questionId.intValue() - 1);
    }

    @Transactional
    public void closeQuiz(Long quizId) {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false).orElseThrow(NoSuchElementException::new);
        quiz.scoreResponse();
        quiz.close();
    }

    public QuizResultValues getQuizResult(Long quizId) {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, true).orElseThrow(NoSuchElementException::new);
        return QuizResultValues.builder()
                .quizId(quizId)
                .quizSetTitle(quiz.getQuizSet().getTitle())
                .quizSetDescription(quiz.getQuizSet().getDescription())
                .songList(quiz.getSongList())
                .responseMap(quiz.getResponseMap())
                .scoreList(quiz.getScoreList())
                .build();
    }

    @Transactional
    public Song saveQuestionResponse(QuestionSubmitValues values) {
        Quiz quiz = quizRepository.findByIdAndClosed(values.getQuizId(), false).orElseThrow(NoSuchElementException::new);
        Song song = songRepository.findById(values.getResponseSongId()).orElseThrow(NoSuchElementException::new);

        quiz.saveResponse(values.getQuestionId() - 1L, song);
        return song;
    }

    public Optional<Quiz> previousQuiz(QuizExistQueryValues values) {
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId()).orElseThrow(IllegalArgumentException::new);
        return quizRepository.findByIdAndQuizSetAndClosed(values.getUserId(), quizSet, false);
    }

    private List<SongFile> sampleSongList(List<SongFile> songPool, Long songNum) {
        Collections.shuffle(songPool);
        return songPool.subList(0, songNum.intValue());
    }

    private void isAllowedToQuizSet(Long userId, QuizSet quizSet) throws IllegalAccessException {
        if (!userId.equals(quizSet.getOwnerId()))
            throw new IllegalAccessException("User { " + userId + " } not allowed to access quiz set { " + quizSet.getId() + " }");
    }

}
