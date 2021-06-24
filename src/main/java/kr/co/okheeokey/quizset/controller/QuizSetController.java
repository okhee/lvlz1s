package kr.co.okheeokey.quizset.controller;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizsets")
public class QuizSetController {
    private final QuizSetService quizSetService;

    @GetMapping
    public ResponseEntity<List<QuizSet>> quizSetList() {
        return ResponseEntity.ok().body(quizSetService.getAllQuizSet());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<QuizSet> getQuizSet(@PathVariable("id") Long id) throws NoSuchElementException {
        return ResponseEntity.ok(quizSetService.getQuizSet(id));
    }

    @PostMapping
    public ResponseEntity<?> newQuizSet(@RequestBody QuizSetAddDto quizSetAddDto) throws IllegalArgumentException {
        QuizSet quizSet = quizSetService.createNewQuizSet(quizSetAddDto);

        return ResponseEntity.created(URI.create("/quizsets/" + quizSet.getId()))
            .build();
    }

}
