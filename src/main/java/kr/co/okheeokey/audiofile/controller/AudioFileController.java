package kr.co.okheeokey.audiofile.controller;

import kr.co.okheeokey.audiofile.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.audiofile.exception.InvalidAudioFormatException;
import kr.co.okheeokey.audiofile.exception.NoAudioFileExistsException;
import kr.co.okheeokey.audiofile.service.AudioFileService;
import kr.co.okheeokey.audiofile.vo.AudioFileSetValues;
import kr.co.okheeokey.audiofile.vo.AudioFileValues;
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
import java.util.ArrayList;
import java.util.List;
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
     *         If the given file can not be read
     * @throws InvalidAudioFormatException
     *         If the given file is not audio file format
     */
    @PostMapping
    public ResponseEntity<?> setAudioFile(@RequestPart MultipartFile file, @RequestParam("q") Long questionId,
          @RequestParam("diff") Long difficulty, @RequestParam(value = "overwrite", defaultValue = "false") Boolean overwrite)
            throws NoSuchElementException, AudioFileAlreadyExistsException, NoAudioFileExistsException, IOException, InvalidAudioFormatException {
        String encryptUuid = audioFileService.setAudioFile(new AudioFileSetValues(file, questionId, difficulty, overwrite));

        return ResponseEntity.created(URI.create("/audiofiles/" + encryptUuid)).build();
    }

    /**
     * POST "/audiofiles/multiple" - Upload and set multiple audiofiles
     * <p>ADMIN authority required</p>
     * <p>{@code difficulty} value must be contiguous with previous values.</p>
     * <p>It will always replace previous audio file, if it exists.</p>
     *
     * @param files Multiple MultipartFile(s) for actual audio file <br>
     *              It can be from HTML &lt;input multiple&gt; tag or {@code curl} with multiple {@code -F "files=@<<some_file>>"} flags <br>
     *              file name must follow certain format <br>
     *              FILENAME: QUESTIONID SPLIT DIFFICULTY FILEEXTENSION <br>
     *              QUESTIONID:    (regex) \D*\d+ <br>
     *              SPLIT:         (regex) [\s_-] <br>
     *              DIFFICULTY:    (regex) \D*\d+ <br>
     *              FILEEXTENSION: (regex) [\.][^\.]* <br>
     *              example: q001-d002.mp3, question123_diff002.flac, "1 2"
     *
     * @return
     * {@code 201 Created} <br>
     * {@code body}: List of {encryptUuid}
     *
     * @throws NoSuchElementException
     *         If the question with id ({@code questionId}) does not exist
     * @throws AudioFileAlreadyExistsException
     *         If {@code overwrite} is false, but there exists an audio file already
     * @throws NoAudioFileExistsException
     *         If {@code overwrite} is true, but there exists no audio file
     * @throws IOException
     *         If the given file can not be read
     * @throws InvalidAudioFormatException
     *         If the given file is not audio file format
     * @throws IllegalArgumentException
     *         If any of specified file has invalid file name.
     */
    @PostMapping("/multiple")
    public ResponseEntity<?> setMultipleAudioFile(@RequestParam("files") MultipartFile[] files)
            throws NoSuchElementException, AudioFileAlreadyExistsException, NoAudioFileExistsException, IOException, IllegalArgumentException {
        List<String> encryptUuidList = new ArrayList<>();
        for (MultipartFile file : files) {
            encryptUuidList.add(audioFileService.setAudioFile(file));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(encryptUuidList);
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
