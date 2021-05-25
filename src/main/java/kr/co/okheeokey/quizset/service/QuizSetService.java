package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.songfile.domain.SongFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizSetService {
    private final QuizSetRepository quizSetRepository;
    private final SongFileRepository songFileRepository;

    public List<QuizSet> getAllQuizSet() {
        return quizSetRepository.findAll();
    }

    public QuizSet getQuizSet(Long quizSetId) throws NoSuchElementException {
        return quizSetRepository.findById(quizSetId).orElseThrow(
                () -> new NoSuchElementException("aa")
        );
    }

    public QuizSet createNewQuizSet(QuizSetCreateValues values) throws IllegalArgumentException{
        List<SongFile> songPool = songFileRepository.findAllById(values.getSongFileIdList());

        if (songPool.size() != values.getSongFileIdList().size())
            throw new IllegalArgumentException("Invalid songFileId input");

        return quizSetRepository.save(
            QuizSet.builder()
                .ownerId(values.getOwnerId())
                .title(values.getTitle())
                .songPool(songPool)
                .description(values.getDescription())
                .build()
        );
    }
}
