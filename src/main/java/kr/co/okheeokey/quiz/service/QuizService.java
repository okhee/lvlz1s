package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.audiofile.exception.NoAudioFileExistsException;
import kr.co.okheeokey.question.domain.Question;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final SongRepository songRepository;
    private final QuizRepository quizRepository;
    private final QuizSetRepository quizSetRepository;

    @Transactional
    public Quiz createNewQuiz(QuizCreateValues values) throws NoSuchElementException, IndexOutOfBoundsException, IllegalAccessException {
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId())
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + values.getQuizSetId() + " }"));
        isAllowedToQuizSet(values.getUser(), quizSet);

        List<Question> questionList = chooseQuestion(quizSet.getQuestionPool(), values.getQuestionNum());

        return quizRepository.findByOwnerAndQuizSetAndClosed(values.getUser(), quizSet, false)
                .orElseGet(() -> {
                    Quiz newQuiz = Quiz.builder()
                            .quizSet(quizSet)
                            .owner(values.getUser())
                            .questionList(questionList)
                            .questionNum(values.getQuestionNum())
                            .closed(false)
                            .build();

                    return quizRepository.save(newQuiz);
                });
    }

    public QuestionInfoValues getQuestion(User user, Long quizId, Long questionIndex)
            throws IndexOutOfBoundsException, NoSuchElementException, IllegalAccessException, NoAudioFileExistsException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));

        isAllowedToQuiz(user, quiz);

        Long hintCost = quiz.isHintAvailable(questionIndex);
        return QuestionInfoValues.builder()
                .encryptUuid(quiz.getAudioFileUuid(questionIndex))
                .hintAvailable(hintCost > 0)
                .nextHintCost(hintCost)
                .build();
    }

    public void getAccessToNewHint(User user, Long quizId, Long questionIndex)
        throws NoSuchElementException, IllegalAccessException, IndexOutOfBoundsException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));
        isAllowedToQuiz(user, quiz);

        quiz.getHint(questionIndex);
    }

    @Transactional
    public Quiz saveQuestionResponse(QuestionSubmitValues values)
            throws IndexOutOfBoundsException, NoSuchElementException, IllegalAccessException {
        Quiz quiz = quizRepository.findByIdAndClosed(values.getQuizId(), false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + values.getQuizId() + " }"));
        isAllowedToQuiz(values.getUser(), quiz);

        Song song = songRepository.findById(values.getResponseSongId())
                .orElseThrow(() -> new NoSuchElementException("No song exists with id { " + values.getResponseSongId() + " }"));

        // if out of bounds, redirect to submit page
        if (values.getQuestionIndex() > quiz.getQuestionNum())
            throw new IndexOutOfBoundsException("Question index { " + values.getQuestionIndex() + " } out of bounds. " +
                    "Current quiz has total " + quiz.getQuestionNum() + " question(s).");

        quiz.saveResponse(values.getQuestionIndex(), song);
        return quiz;
    }

    public QuizStatusValues getQuizStatus(User user, Long quizId) throws NoSuchElementException, IllegalAccessException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NoSuchElementException("No quiz exists with id { " + quizId + " }"));

        isAllowedToQuiz(user, quiz);

        return new QuizStatusValues(quiz, quiz.getQuizSet());
    }

    @Transactional
    public void closeQuiz(User user, Long quizId) throws NoSuchElementException, IllegalAccessException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));
        isAllowedToQuiz(user, quiz);

        quiz.scoreResponse();
        quiz.close();
    }

    @Transactional
    public void deleteQuiz(User user, Long quizId) throws NoSuchElementException, IllegalAccessException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));
        isAllowedToQuiz(user, quiz);

        quizRepository.deleteById(quiz.getId());
    }

    private List<Question> chooseQuestion(List<Question> questionPool, Long questionNum) throws IndexOutOfBoundsException {
        Collections.shuffle(questionPool);
        return questionPool.subList(0, questionNum.intValue());
    }

    private void isAllowedToQuiz(User user, Quiz quiz) throws IllegalAccessException {
        if (user.equals(quiz.getOwner()))
            return;
        throw new IllegalAccessException("User { " + user.getName() + " } not allowed to access quiz { " + quiz.getId() + " }");
    }

    private void isAllowedToQuizSet(User user, QuizSet quizSet) throws IllegalAccessException {
        if (quizSet.getReadyMade())
            return;
        if (user.equals(quizSet.getOwner()))
            return;
        throw new IllegalAccessException("User { " + user.getName() + " } not allowed to access quiz set { " + quizSet.getId() + " }");
    }

}
