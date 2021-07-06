package kr.co.okheeokey.quizset.controller;

import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.quizset.vo.QuizSetInfoValues;
import kr.co.okheeokey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller class of "/audiofiles/**" endpoints
 *
 * @see QuizSetService
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/quizsets")
public class QuizSetController {
    private final QuizSetService quizSetService;

    /**
     * GET "/quizsets" - Get  list of information of available (ready-made and user-made) quiz sets
     * <p>If {@code user} is authenticated, also get quiz sets that are made by {@code user}</p>
     *
     * @param user Not required
     *
     * @return
     * {@code 200 OK} <br>
     * {@code List<QuizSetInfoValues>}
     */
    @GetMapping
    public ResponseEntity<List<QuizSetInfoValues>> getAllQuizSet(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(quizSetService.getAllQuizSet(user));
    }

    /**
     * GET "/quizsets/{quizSetId}" - Get information of a quiz set
     * <p>If the quiz set with id {@code quizSetId} is made by user, {@code user} must be authenticated and is owner of such quiz set</p>
     *
     * @param user Not required
     * @param quizSetId Id of {@code quizset}
     *
     * @return
     * {@code 200 OK} <br>
     * {@code QuizSetInfoValues}
     *
     * @throws NoSuchElementException
     *         If the quiz set with id ({@code quizSetId}) does not exist
     * @throws IllegalAccessException
     *         If the quiz set is neither ready-made nor owned by {@code user}
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<QuizSetInfoValues> getQuizSet(@AuthenticationPrincipal User user, @PathVariable("id") Long quizSetId)
            throws NoSuchElementException, IllegalAccessException {
        return ResponseEntity.ok(quizSetService.getQuizSet(user, quizSetId));
    }

    /**
     * POST "/quizsets" - create new quiz set
     * <p>Create new quiz set using {@link QuizSetAddDto} information <br> </p>
     *
     * @param user [Authenticated]
     * @param quizSetAddDto <br>
     * {@code title} - Title of quiz set <br>
     * {@code description} - Description of quiz set <br>
     * {@code albumIdList} - List of {@code albumId}(s) to be included <br>
     * {@code songIdList} - List of {@code songId}(s) to be included <br>
     * {@code easy} - Include {@code QuestionDifficulty.EASY} questions <br>
     * {@code medium} - Include {@code QuestionDifficulty.MEDIUM} questions <br>
     * {@code hard} - Include {@code QuestionDifficulty.HARD} questions
     * <p> It does not check whether given {@code albumId}(s) or {@code songId}(s) are valid or not </p> <br>
     * <p> First, from chosen album(s), add all of its songs to list. <br>
     * Second, add chosen song(s) to list <br>
     * Third, get every question related to list of songs <br>
     * Last, filter questions using difficulty flags (easy, medium, hard)</p>
     *
     * @return
     * {@code 201 Created} <br>
     * {@code Location}: "/quizsets/{quizSetId}"
     *
     * @throws IllegalArgumentException
     *         If no question is selected
     *
     * @see kr.co.okheeokey.quiz.controller.QuizController#createQuiz(User, QuizCreateDto)
     */
    @PostMapping
    public ResponseEntity<?> newQuizSet(@AuthenticationPrincipal User user, @RequestBody QuizSetAddDto quizSetAddDto)
            throws IllegalArgumentException {
        QuizSet quizSet = quizSetService.createNewQuizSet(user, new QuizSetCreateValues(quizSetAddDto));

        return ResponseEntity.created(URI.create("/quizsets/" + quizSet.getId()))
            .build();
    }

}
