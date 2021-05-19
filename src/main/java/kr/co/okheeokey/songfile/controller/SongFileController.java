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
    public ResponseEntity<?> setContent(@RequestPart MultipartFile file,
                                        @PathVariable("id") Long id,
                                        @RequestParam(value = "diff", defaultValue = "0") Long difficulty) throws Exception{
//        Long difficulty = 0L;
//        for(MultipartFile file: files) {
//            File f = new File("src/main/resources/static/audio/test", file.getOriginalFilename());
//            new ClassPathResource("static/audio/test" + file.getOriginalFilename())
//            try {
//                f.createNewFile();
//                file.transferTo(f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        songFileService.saveAudioFile(id, file, difficulty);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/songfiles/" + id + "?diff=" + difficulty);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable("id") Long id,
                                               @RequestParam(value = "diff", defaultValue = "0") Long difficulty) throws IOException{
        byte[] binaryAudioFile = songFileService.getAudioFile(id, difficulty);
        return new ResponseEntity<>(binaryAudioFile, HttpStatus.OK);
    }
}
