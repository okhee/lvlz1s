package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizQueryValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.song.SongFile;
import kr.co.okheeokey.user.User;
import kr.co.okheeokey.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizService {
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
                        .responseList(null)
                        .closed(false)
                        .build()
        );
    }

    public SongFile getQuestion(Long quizId, Long questionIndex) {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false).orElseThrow(IllegalArgumentException::new);
        return quiz.getSongList().get(questionIndex.intValue() - 1);
    }

    public Optional<Quiz> previousQuiz(QuizQueryValues values) {
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId()).orElseThrow(IllegalArgumentException::new);
        return quizRepository.findByIdAndQuizSetAndClosed(values.getUserId(), quizSet, false);
    }

    private List<SongFile> sampleSongList(List<SongFile> songPool, Long songNum) {
        Collections.shuffle(songPool);
        return songPool.subList(0, songNum.intValue());
    }
}
