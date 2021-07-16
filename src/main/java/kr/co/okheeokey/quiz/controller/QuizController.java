package kr.co.okheeokey.quiz.controller;

import kr.co.okheeokey.audiofile.controller.AudioFileController;
import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.QuestionInfoValues;
import kr.co.okheeokey.quiz.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizStatusValues;
import kr.co.okheeokey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller class of "/quiz/**" endpoints
 *
 * @see QuizService
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/quiz")
public class QuizController {
    private final QuizService quizService;

    /**
     * POST "/quiz" - Create new quiz instance
     *
     * <p> If user has previous ongoing quiz with same quiz set id,
     * return such quiz instance instead </p>
     *
     * @param user [Authenticated]
     * @param dto {@code quizSetId}, {@code questionNum}
     *
     * @return
     * {@code 202 Accepted} <br>
     * {@code Location}: "/quiz/{quizId}"
     *
     * @throws NoSuchElementException
     *         If {@code quizSetId} is invalid
     * @throws IllegalAccessException
     *         If chosen {@code quizset} is neither ready-made nor created by {@code user}
     * @throws IndexOutOfBoundsException
     *         If {@code questionNum} is greater than size of {@code questionPool} of quizset
     */
    @PostMapping
    public ResponseEntity<?> createQuiz(@AuthenticationPrincipal User user, @RequestBody QuizCreateDto dto)
            throws NoSuchElementException, IllegalAccessException, IndexOutOfBoundsException {
        Quiz quiz = quizService.createNewQuiz(new QuizCreateValues(user, dto));

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .location(URI.create("/quiz/" + quiz.getId())).build();
    }

    /**
     * GET "/quiz/{quizId}" - Get information of ongoing/finished quiz instance
     *
     * @param user [Authenticated]
     * @param quizId Id of {@code quiz}
     * @return
     * {@code 200 OK} <br>
     * {@link QuizStatusValues}
     * @throws NoSuchElementException
     *         If {@code quizId} is invalid
     * @throws IllegalAccessException
     *         If {@code user} is not owner of quiz
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizInfo(@AuthenticationPrincipal User user,
            @PathVariable("id") Long quizId) throws NoSuchElementException, IllegalAccessException {
        QuizStatusValues quizStatus = quizService.getQuizStatus(user, quizId);

        return ResponseEntity.ok().body(
                EntityModel.of(quizStatus,
                        linkTo(methodOn(QuizController.class).getQuizInfo(user, quizId)).withSelfRel())
        );
    }

    /**
     * GET "/quiz/{quizId}/q/{questionIndex}" - Get question info
     *
     * @param user [Authenticated]
     * @param quizId Id of {@code quiz}
     * @param questionIndex 1-based indexing; Index of question from {@code questionList}
     *
     * @return
     * {@code 200 OK} <br>
     * {@link QuestionInfoValues} - {@code encryptUuid}, {@code hintAvailable}, {@code nextHintCost}
     * <p>{@code encryptUuid} - Encrypted UUID value; "/audiofiles/{encryptUuid}" </p>
     * <p>{@code hintAvailable} - true or false; whether further hint audiofile exists</p>
     * <p>{@code nextHintCost} - (1, 2, ...) or -1; Cost to grant additional hint, if none exists, -1</p>
     *
     * @throws IndexOutOfBoundsException
     *         If {@code questionIndex} is greater than {@code questionNum}
     * @throws NoSuchElementException
     *         If ongoing quiz with id ({@code quizId}) does not exist
     * @throws IllegalAccessException
     *         If {@code user} is not owner of quiz
     *
     * @see AudioFileController#getAudioFile(String)
     */
    @GetMapping("/{id}/q/{qid}")
    public ResponseEntity<?> getQuestion(@AuthenticationPrincipal User user,
            @PathVariable("id") Long quizId, @PathVariable("qid") Long questionIndex)
            throws IndexOutOfBoundsException, NoSuchElementException, IllegalAccessException {
        QuestionInfoValues values = quizService.getQuestion(user, quizId, questionIndex);

        return ResponseEntity.ok().body(
                EntityModel.of(values,
                linkTo(methodOn(QuizController.class).getQuestion(user, quizId, questionIndex)).withSelfRel(),
                linkTo(methodOn(QuizController.class).submitQuestion(user, quizId, questionIndex, null)).withRel("submit"))
        );
    }

    /**
     * POST "/quiz/{quizId}/q/{questionIndex}/hint" - Request additional hint for question
     *
     * @param user [Authenticated]
     * @param quizId Id of {@code quiz}
     * @param questionIndex 1-based indexing; Index of question from {@code questionList} that user wants hint for
     *
     * @return
     * {@code 301 Moved Permanently} <br>
     * {@code Location}: "/quiz/{quizId}/q/{questionIndex}"
     *
     * @throws IndexOutOfBoundsException
     *         If {@code questionIndex} is greater than {@code questionNum}
     * @throws IllegalAccessException
     *         If {@code user} is not owner of quiz
     * @throws NoSuchElementException
     *         If ongoing quiz with id ({@code quizId}) does not exist
     *
     * @see QuizController#getQuestion(User, Long, Long) 
     */
    @PostMapping("/{id}/q/{qid}/hint")
    public ResponseEntity<?> getHint(@AuthenticationPrincipal User user,
            @PathVariable("id") Long quizId, @PathVariable("qid") Long questionIndex)
            throws IndexOutOfBoundsException, IllegalAccessException, NoSuchElementException {
        quizService.getAccessToNewHint(user, quizId, questionIndex);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/quiz/" + quizId + "/q/" + questionIndex))
                .build();
    }

    /**
     * POST "/quiz/{quizId}/q/{questionIndex}" - Submit answer response of question
     *
     * @param user [Authenticated]
     * @param quizId Id of {@code quiz}
     * @param questionIndex 1-based indexing; Index of question from {@code questionList}
     * @param submitDto
     *        {@code responseSongId}: Id of {@code song}
     * @return
     * {@code 202 Accepted} <br>
     *
     * @throws NoSuchElementException
     *         If ongoing quiz with id ({@code quizId}) does not exist or {@code songId} is invalid
     * @throws IndexOutOfBoundsException
     *         If {@code questionIndex} is greater than {@code questionNum}
     * @throws IllegalAccessException
     *         If {@code user} is not owner of quiz
     */
    @PostMapping("/{id}/q/{qid}")
    public ResponseEntity<?> submitQuestion(@AuthenticationPrincipal User user, @PathVariable("id") Long quizId,
            @PathVariable("qid") Long questionIndex, @RequestBody QuestionSubmitDto submitDto)
            throws NoSuchElementException, IndexOutOfBoundsException, IllegalAccessException {
        quizService.saveQuestionResponse(new QuestionSubmitValues(user, quizId, questionIndex, submitDto.getResponseSongId()));

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).getQuestion(user, quizId, questionIndex+1)).withRel(IanaLinkRelations.NEXT))
        );
    }

    /**
     * POST "/quiz/{quizId}" - Finish and submit quiz instance
     * <p>It accepts either every question is answered or not</p>
     *
     * @param user [Authenticated]
     * @param quizId Id of {@code quiz}
     * @return
     * {@code 202 Accepted} <br>
     *
     * @throws NoSuchElementException
     *         If ongoing quiz with id ({@code quizId}) does not exist
     * @throws IllegalAccessException
     *         If {@code user} is not owner of quiz
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> submitQuiz(@AuthenticationPrincipal User user, @PathVariable("id") Long quizId)
            throws NoSuchElementException, IllegalAccessException {
        quizService.closeQuiz(user, quizId);

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).getQuizInfo(user, quizId)).withRel(IanaLinkRelations.NEXT))
        );
    }

    /**
     * DELETE "/quiz/{quizId}" - Give up ongoing quiz instance
     * <p>It has no effect on finished quiz instance.</p>
     *
     * @param user [Authenticated]
     * @param quizId Id of {@code quiz}
     *
     * @return
     * {@code 204 No Content} <br>
     *
     * @throws NoSuchElementException
     *         If ongoing quiz with id ({@code quizId}) does not exist
     * @throws IllegalAccessException
     *         If {@code user} is not owner of quiz
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> giveUpQuiz(@AuthenticationPrincipal User user, @PathVariable("id") Long quizId)
            throws NoSuchElementException, IllegalAccessException {
        quizService.deleteQuiz(user, quizId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
