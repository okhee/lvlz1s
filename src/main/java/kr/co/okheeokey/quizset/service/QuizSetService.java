package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class QuizSetService {
    private final QuizSetRepository quizSetRepository;
    private final QuestionRepository questionRepository;

    public List<QuizSet> getAllQuizSet() {
        return quizSetRepository.findAll();
    }

    public QuizSet getQuizSet(Long quizSetId) throws NoSuchElementException {
        return quizSetRepository.findById(quizSetId).orElseThrow(
                () -> new NoSuchElementException("aa")
        );
    }

    public QuizSet createNewQuizSet(QuizSetCreateValues values) throws IllegalArgumentException{
        List<Question> songPool = questionRepository.findAllById(values.getQuestionIdList());

        if (songPool.size() != values.getQuestionIdList().size())
            throw new IllegalArgumentException("Invalid questionId input");

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
