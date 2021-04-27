package kr.co.okheeokey.quizset.controller;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizsets")
public class QuizSetController {
    private final QuizSetService quizSetService;

    @GetMapping
    public ResponseEntity<List<QuizSet>> quizSetList() {
        return ResponseEntity.ok().body(quizSetService.getAllQuizSet());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newQuizSet(@RequestBody QuizSetAddDto quizSetAddDto) {
        QuizSet quizSet = quizSetService.createNewQuizSet(new QuizSetCreateValues(quizSetAddDto));

        return ResponseEntity.accepted().body(quizSet);
    }


}
