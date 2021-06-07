package kr.co.okheeokey.question.controller;

import kr.co.okheeokey.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> setAudioFile(@RequestPart MultipartFile file,
                                          @PathVariable("id") Long questionId,
                                          @RequestParam(value = "diff", defaultValue = "0") Long difficulty) throws Exception{
        questionService.setAudioFile(questionId, file, difficulty);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/questions/" + questionId + "?diff=" + difficulty);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable("id") Long id,
                                               @RequestParam(value = "diff", defaultValue = "0") Long difficulty) throws IOException{
        byte[] binaryAudioFile = questionService.getAudioFile(id, difficulty);
        return new ResponseEntity<>(binaryAudioFile, HttpStatus.OK);
    }
}
