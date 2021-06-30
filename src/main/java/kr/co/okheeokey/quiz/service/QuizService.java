package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.question.domain.Question;
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
import kr.co.okheeokey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final SongRepository songRepository;
    private final QuizRepository quizRepository;
    private final QuizSetRepository quizSetRepository;

    // Check if previous ongoing quiz exists using (User, QuizSet)
    // If exist, return such quiz
    public Optional<Quiz> previousQuiz(QuizExistQueryValues values) throws NoSuchElementException, IllegalAccessException {
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId())
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + values.getQuizSetId() + " }"));
        isAllowedToQuizSet(values.getUser(), quizSet);

        return quizRepository.findByOwnerAndQuizSetAndClosed(values.getUser(), quizSet, false);
    }

    public Quiz createNewQuiz(QuizCreateValues values) throws NoSuchElementException, IndexOutOfBoundsException {
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId())
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + values.getQuizSetId() + " }"));

        List<Question> randomQuestionList = chooseQuestion(quizSet.getQuestionPool(), values.getQuestionNum());

        Quiz newQuiz = Quiz.builder()
                            .quizSet(quizSet)
                            .owner(values.getUser())
                            .questionList(randomQuestionList)
                            .questionNum(values.getQuestionNum())
                            .closed(false)
                            .build();
        return quizRepository.save(newQuiz);
    }

    public Question getQuestion(User user, Long quizId, Long questionId)
            throws IndexOutOfBoundsException, NoSuchElementException, IllegalAccessException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));

        isAllowedToQuiz(user, quiz);

        return quiz.getQuestionList().get(questionId.intValue() - 1);
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
        if (values.getQuestionId() > quiz.getQuestionNum())
            throw new IndexOutOfBoundsException("Question index { " + values.getQuestionId() + " } out of bounds. " +
                    "Current quiz has total " + quiz.getQuestionNum() + " question(s).");

        quiz.saveResponse(values.getQuestionId() - 1L, song);
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

    private void isAllowedToQuiz(User user, Quiz quiz) throws IllegalAccessException{
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
