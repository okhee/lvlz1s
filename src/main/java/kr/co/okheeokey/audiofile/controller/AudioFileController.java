package kr.co.okheeokey.audiofile.controller;

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

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/audiofiles")
public class AudioFileController {
    private final AudioFileService audioFileService;

    @PostMapping
    public ResponseEntity<?> setAudioFile(@RequestPart MultipartFile file, @RequestParam("q") Long questionId,
          @RequestParam("diff") Long difficulty, @RequestParam(value = "update", defaultValue = "false") Boolean overwrite)
            throws Exception {
        String encryptUuid = audioFileService.setAudioFile(new AudioFileSetValues(file, questionId, difficulty, overwrite));

        return ResponseEntity.created(URI.create("/audiofiles/" + encryptUuid)).build();
    }

    @GetMapping("/{enc_uuid}")
    public ResponseEntity<?> getAudioFile(@PathVariable("enc_uuid") String encryptUuid) throws Exception {
        AudioFileValues values = audioFileService.getAudioFile(encryptUuid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(values.getMimeType()));
        headers.setContentLength(values.getContentLength());

        return new ResponseEntity<>(new InputStreamResource(values.getAudioStream()), headers, HttpStatus.OK);
    }

}
