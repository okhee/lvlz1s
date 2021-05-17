package kr.co.okheeokey.songfile.controller;

import kr.co.okheeokey.songfile.service.SongFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@RestController("/songfiles")
public class SongFileController {
    private final SongFileService songFileService;

    @PostMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> setContent(@PathVariable("id") Long id, @RequestParam("files") MultipartFile[] files) {
        Long difficulty = 0L;
        for(MultipartFile file: files) {
//            File f = new File("src/main/resources/static/audio/test", file.getOriginalFilename());
//            new ClassPathResource("static/audio/test" + file.getOriginalFilename())
//            try {
//                f.createNewFile();
//                file.transferTo(f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            try {
                songFileService.saveSongFile(id, file, difficulty++);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable("id") Long id, @RequestParam("diff") Long difficulty) {
        byte[] binaryAudioFile = songFileService.getAudioFile(id, difficulty);
        return new ResponseEntity<>(binaryAudioFile, HttpStatus.OK);
    }
}
