package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.song.SongFile;
import kr.co.okheeokey.song.SongFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuizSetService {
    private final QuizSetRepository quizSetRepository;
    private final SongFileRepository songFileRepository;

    public List<QuizSet> getAllQuizSet() {
        return quizSetRepository.findAll();
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
