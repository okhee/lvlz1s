package kr.co.okheeokey.quiz.controller;

import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.song.SongFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizs")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> resumeQuiz(@PathVariable("id") Long id,
                                        @RequestParam(value = "q", required = false, defaultValue = "1") Long qid){
        SongFile question = quizService.getQuestion(id, qid);
        return ResponseEntity.ok().body(Collections.singletonMap("filename", question.getSongFileName()));
    }
}
