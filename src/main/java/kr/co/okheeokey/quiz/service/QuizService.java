package kr.co.okheeokey.quiz.service;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.domain.QuizRepository;
import kr.co.okheeokey.quiz.vo.*;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.user.User;
import kr.co.okheeokey.user.UserRepository;
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
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizSetRepository quizSetRepository;

    // Check if previous ongoing quiz exists using (User, QuizSet)
    // If exist, return such quiz
    public Optional<Quiz> previousQuiz(QuizExistQueryValues values) throws NoSuchElementException, IllegalAccessException {
        User user = userRepository.findById(values.getUserId())
                .orElseThrow(() -> new NoSuchElementException("No user exists with id { " + values.getUserId() + " }"));
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId())
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + values.getQuizSetId() + " }"));
        isAllowedToQuizSet(values.getUserId(), quizSet);

        return quizRepository.findByOwnerAndQuizSetAndClosed(user, quizSet, false);
    }

    public Quiz createNewQuiz(QuizCreateValues values) throws NoSuchElementException, IndexOutOfBoundsException {
        User user = userRepository.findById(values.getUserId())
                .orElseThrow(() -> new NoSuchElementException("No user exists with id { " + values.getUserId() + " }"));
        QuizSet quizSet = quizSetRepository.findById(values.getQuizSetId())
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + values.getQuizSetId() + " }"));

        List<Question> randomQuestionList = chooseQuestion(quizSet.getQuestionPool(), values.getQuestionNum());

        Quiz newQuiz = Quiz.builder()
                            .quizSet(quizSet)
                            .owner(user)
                            .questionList(randomQuestionList)
                            .questionNum(values.getQuestionNum())
                            .closed(false)
                            .build();
        return quizRepository.save(newQuiz);
    }

    public Question getQuestion(Long quizId, Long questionId) throws IndexOutOfBoundsException, NoSuchElementException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));

        return quiz.getQuestionList().get(questionId.intValue() - 1);
    }

    @Transactional
    public Quiz saveQuestionResponse(QuestionSubmitValues values) throws IndexOutOfBoundsException, NoSuchElementException {
        Quiz quiz = quizRepository.findByIdAndClosed(values.getQuizId(), false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + values.getQuizId() + " }"));
        Song song = songRepository.findById(values.getResponseSongId())
                .orElseThrow(() -> new NoSuchElementException("No song exists with id { " + values.getResponseSongId() + " }"));

        // if out of bounds, redirect to submit page
        if (values.getQuestionId() > quiz.getQuestionNum())
            throw new IndexOutOfBoundsException("Question index { " + values.getQuestionId() + " } out of bounds. " +
                    "Current quiz has total " + quiz.getQuestionNum() + " question(s).");

        quiz.saveResponse(values.getQuestionId() - 1L, song);
        return quiz;
    }

    public QuizStatusValues getQuizStatus(Long quizId) throws NoSuchElementException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NoSuchElementException("No quiz exists with id { " + quizId + " }"));

        return new QuizStatusValues(quiz, quiz.getQuizSet());
    }

    @Transactional
    public void closeQuiz(Long quizId) throws NoSuchElementException{
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));

        quiz.scoreResponse();
        quiz.close();
    }

    // Todo: check user authorization
    @Transactional
    public void deleteQuiz(Long quizId) throws NoSuchElementException {
        Quiz quiz = quizRepository.findByIdAndClosed(quizId, false)
                .orElseThrow(() -> new NoSuchElementException("No ongoing quiz exists with id { " + quizId + " }"));
        quizRepository.deleteById(quiz.getId());
    }

    private List<Question> chooseQuestion(List<Question> questionPool, Long questionNum) throws IndexOutOfBoundsException {
        Collections.shuffle(questionPool);
        return questionPool.subList(0, questionNum.intValue());
    }

    private void isAllowedToQuizSet(Long userId, QuizSet quizSet) throws IllegalAccessException {
        if (quizSet.getReadyMade())
            return;
        if (!userId.equals(quizSet.getOwnerId()))
            throw new IllegalAccessException("User { " + userId + " } not allowed to access quiz set { " + quizSet.getId() + " }");
    }

}
