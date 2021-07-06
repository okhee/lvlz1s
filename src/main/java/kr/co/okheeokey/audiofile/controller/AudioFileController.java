package kr.co.okheeokey.audiofile.controller;

import kr.co.okheeokey.audiofile.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.audiofile.exception.NoAudioFileExistsException;
import kr.co.okheeokey.audiofile.service.AudioFileService;
import kr.co.okheeokey.audiofile.vo.AudioFileSetValues;
import kr.co.okheeokey.question.vo.AudioFileValues;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.NoSuchElementException;

/**
 * Controller class of "/audiofiles/**" endpoints
 *
 * @see AudioFileService
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/audiofiles")
public class AudioFileController {
    private final AudioFileService audioFileService;

    /**
     * POST "/audiofiles" - Upload and set audiofile
     * <p>ADMIN authority required</p>
     * <p>{@code difficulty} value must be contiguous with previous values.</p>
     * <p>Overwriting existing audio file is possible</p>
     *
     * @param file MultipartFile for actual audio file
     * @param questionId Id of {@code question}
     * @param difficulty 0-based indexing; Greater the {@code difficulty} value, the longer the audio file is.
     * @param overwrite Overwrite existing audio file
     *
     * @return
     * {@code 201 Created} <br>
     * {@code Location}: "/audiofiles/{encryptUuid}"
     *
     * @throws NoSuchElementException
     *         If the question with id ({@code questionId}) does not exist
     * @throws AudioFileAlreadyExistsException
     *         If {@code overwrite} is false, but there exists an audio file already
     * @throws NoAudioFileExistsException
     *         If {@code overwrite} is true, but there exists no audio file
     * @throws IOException
     *         If the given audio file is an invalid file
     */
    @PostMapping
    public ResponseEntity<?> setAudioFile(@RequestPart MultipartFile file, @RequestParam("q") Long questionId,
          @RequestParam("diff") Long difficulty, @RequestParam(value = "overwrite", defaultValue = "false") Boolean overwrite)
            throws NoSuchElementException, AudioFileAlreadyExistsException, NoAudioFileExistsException, IOException {
        String encryptUuid = audioFileService.setAudioFile(new AudioFileSetValues(file, questionId, difficulty, overwrite));

        return ResponseEntity.created(URI.create("/audiofiles/" + encryptUuid)).build();
    }

    /**
     * GET "/audiofiles/{encryptUuid}" - Get InputStreamResource of specific audio file
     * <p>Audio file is identified with 'encrypted' uuid value <br>
     * This URL can be directly used in 'src' value of HTML audio tag <br>
     * e.g. &lt;audio src="/audiofiles/fj23olifj23o="&gt;</p>
     *
     * @param encryptUuid 'encrypted' uuid value of specific audio file
     *
     * @return
     * {@code 200 OK} <br>
     * {@link InputStreamResource} of an audio file
     *
     * @throws IllegalArgumentException
     *         If 'encryptUuid' value is invalid
     */
    @GetMapping("/{encryptUuid}")
    public ResponseEntity<?> getAudioFile(@PathVariable("encryptUuid") String encryptUuid)
            throws IllegalArgumentException {
        AudioFileValues values = audioFileService.getAudioFile(encryptUuid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(values.getMimeType()));
        headers.setContentLength(values.getContentLength());

        return new ResponseEntity<>(new InputStreamResource(values.getAudioStream()), headers, HttpStatus.OK);
    }

}
