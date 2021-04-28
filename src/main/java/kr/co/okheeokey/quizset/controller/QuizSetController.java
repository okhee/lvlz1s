package kr.co.okheeokey.quizset.controller;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizQueryValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizsets")
public class QuizSetController {
//    private final UserService userService;
    private final QuizService quizService;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> startQuiz(@PathVariable("id") Long quizSetId) {
        // check authority
        Long userId = 1L;

        try {
            quizSetService.isAllowed(userId, quizSetId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        Quiz quiz = quizService.previousQuiz(new QuizQueryValues(userId, quizSetId))
                .orElseGet(() -> quizService.createNewQuiz(new QuizCreateValues(userId, quizSetId, 2L)));

        URI uri = URI.create("/quizs/" + quiz.getId());
        return ResponseEntity.created(uri).build();
    }

}
