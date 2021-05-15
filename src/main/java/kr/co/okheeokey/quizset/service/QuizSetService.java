package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.song.domain.SongFile;
import kr.co.okheeokey.song.domain.SongFileRepository;
import kr.co.okheeokey.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizSetService {
    private final QuizSetRepository quizSetRepository;
    private final SongFileRepository songFileRepository;

    public List<QuizSet> getAllQuizSet() {
        return quizSetRepository.findAll();
    }

    public Optional<QuizSet> getQuizSet(Long quizSetId) {
        return quizSetRepository.findById(quizSetId);
    }

    public void isAllowed(Long userId, Long quizSetId) throws Exception {
        Optional<QuizSet> quizSet = quizSetRepository.findById(quizSetId);

        if (quizSet.isEmpty())
            throw new IllegalArgumentException();
        if (!userId.equals(quizSet.get().getOwnerId()))
            throw new IllegalAccessException();
    }

    public QuizSet createNewQuizSet(QuizSetCreateValues values) {
        List<SongFile> songPool = songFileRepository.findAllById(values.getSongFileIdList());

        return quizSetRepository.save(
                QuizSet.builder()
                        .ownerId(values.getOwnerId())
                        .title(values.getTitle())
                        .songPool(songPool)
                        .title(values.getTitle())
                        .description(values.getDescription())
                        .build()
        );
    }
}
