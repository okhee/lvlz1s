package kr.co.okheeokey.question.controller;

import kr.co.okheeokey.audiofile.controller.AudioFileController;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.question.service.QuestionService;
import kr.co.okheeokey.question.vo.QuestionInfoValues;
import kr.co.okheeokey.question.vo.QuestionResultValues;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.song.vo.AlbumInfoValues;
import kr.co.okheeokey.song.vo.SongInfoValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    /**
     * POST "/question?difficulty={difficulty}" - get random question information with specified difficulty
     *
     * @param difficulty EASY, MEDIUM, HARD - {@link QuestionDifficulty}
     *
     * @return
     * {@code 200 OK} <br>
     * {@code QuestionInfoValues} - {@code encryptUuid}, {@code hintAvailable}, {@code nextHintCost}
     * <p>{@code encryptUuid} - Encrypted UUID value; "/audiofiles/{encryptUuid}"</p>
     * <p>{@code hintAvailable} - {@code true} or {@code false}; whether further hint audiofile exists</p>
     * <p>{@code nextHintCost} - {@code 0} if hint is available, {@code -1} if not</p>
     *
     * @throws NoSuchElementException
     *         If there does not exist corresponding {@code question} or {@code audiofile}.
     *
     * @see AudioFileController#getAudioFile(String)
     */
    @PostMapping
    public ResponseEntity<?> createRandomQuestionInstance(@RequestParam(value = "difficulty", defaultValue = "MEDIUM") QuestionDifficulty difficulty)
            throws NoSuchElementException {
        QuestionInfoValues values = questionService.getRandomQuestion(difficulty);

        return ResponseEntity.ok().body(values);
    }

    /**
     * POST "/question/hint/{encryptUuid}" - Request additional hint for question
     *
     * @param encryptUuid encryptUuid of current audio file
     *
     * @return
     * {@code 200 OK} <br>
     * {@code QuestionInfoValues} - {@code encryptUuid}, {@code hintAvailable}, {@code nextHintCost}
     * <p>{@code encryptUuid} - Encrypted UUID value; "/audiofiles/{encryptUuid}"</p>
     * <p>{@code hintAvailable} - {@code true} or {@code false}; whether further hint audiofile exists</p>
     * <p>{@code nextHintCost} - {@code 0} if hint is available, {@code -1} if not</p>
     *
     * @throws NoSuchElementException
     *         If there does not exist corresponding {@code question} or {@code audiofile}.
     *
     * @see AudioFileController#getAudioFile(String)
     */
    @PostMapping("/hint/{encryptUuid}")
    public ResponseEntity<?> getQuestionHint(@PathVariable("encryptUuid") String encryptUuid)
            throws NoSuchElementException {
        QuestionInfoValues values = questionService.getQuestionHint(encryptUuid);

        return ResponseEntity.ok().body(values);
    }

    /**
     * POST "/question/submit/{encryptUuid}" - Submit answer response of question
     *
     * @param encryptUuid encryptUuid of current audio file
     * @param submitDto
     *        {@code responseSongId}: Id of {@code song}
     *
     * @return
     * {@code 200 OK} <br>
     * {@code QuestionResultValues} - {@link SongInfoValues}, {@code correct}, {@code youtubeAddress}
     * {@code SongInfoValues} - {@code songName}, {@link AlbumInfoValues} <br>
     *      Information of 'submitted/selected' song. It does not show answer song. <br>
     * {@code AlbumInfoValues} - {@code albumName}, {@code releasedDate}, {@code albumCover} <br>
     * {@code correct} - Whether answer response was correct <br>
     * {@code youtubeAddress} - "https://www.youtube.com/embed/{address}?start={location}s"
     *
     * @throws NoSuchElementException
     *         If there does not exist corresponding {@code question} or {@code audiofile}.
     *
     */
    @PostMapping("/submit/{encryptUuid}")
    public ResponseEntity<?> submitQuestionAnswer(@PathVariable("encryptUuid") String encryptUuid,
                                                  @RequestBody QuestionSubmitDto submitDto)
            throws NoSuchElementException {
        QuestionResultValues values = questionService.submitResponse(encryptUuid, submitDto.getResponseSongId());

        return ResponseEntity.ok().body(values);
    }

}
