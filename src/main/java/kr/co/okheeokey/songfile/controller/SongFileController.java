package kr.co.okheeokey.songfile.controller;

import kr.co.okheeokey.songfile.service.SongFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class SongFileController {
    private final SongFileService songFileService;

    @PostMapping("/songfiles/{id}")
    public ResponseEntity<?> setContent(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        try {
            songFileService.saveSongFile(id, file);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
