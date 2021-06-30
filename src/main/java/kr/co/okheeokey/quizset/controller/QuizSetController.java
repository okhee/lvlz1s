package kr.co.okheeokey.quizset.controller;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<QuizSet>> quizSetList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(quizSetService.getAllQuizSet(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<QuizSet> getQuizSet(@AuthenticationPrincipal User user, @PathVariable("id") Long id)
            throws NoSuchElementException, IllegalAccessException {
        return ResponseEntity.ok(quizSetService.getQuizSet(user, id));
    }

    @PostMapping
    public ResponseEntity<?> newQuizSet(@AuthenticationPrincipal User user, @RequestBody QuizSetAddDto quizSetAddDto)
            throws IllegalArgumentException {
        QuizSet quizSet = quizSetService.createNewQuizSet(user, new QuizSetCreateValues(quizSetAddDto));

        return ResponseEntity.created(URI.create("/quizsets/" + quizSet.getId()))
            .build();
    }

}
