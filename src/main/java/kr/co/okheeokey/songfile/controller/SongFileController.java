package kr.co.okheeokey.songfile.controller;

import kr.co.okheeokey.songfile.service.SongFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/songfiles")
public class SongFileController {
    private final SongFileService songFileService;

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> setAudioFile(@RequestPart MultipartFile file,
                                          @PathVariable("id") Long songFileId,
                                          @RequestParam(value = "diff", defaultValue = "0") Long difficulty) throws Exception{
        songFileService.setAudioFile(songFileId, file, difficulty);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/songfiles/" + songFileId + "?diff=" + difficulty);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable("id") Long id,
                                               @RequestParam(value = "diff", defaultValue = "0") Long difficulty) throws IOException{
        byte[] binaryAudioFile = songFileService.getAudioFile(id, difficulty);
        return new ResponseEntity<>(binaryAudioFile, HttpStatus.OK);
    }
}
