package kr.co.okheeokey.question.controller;

import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.question.service.QuestionService;
import kr.co.okheeokey.question.vo.QuestionInfoValues;
import kr.co.okheeokey.question.vo.QuestionResultValues;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<?> createRandomQuestionInstance(@RequestParam(value = "difficulty", defaultValue = "MEDIUM") QuestionDifficulty difficulty) {
        QuestionInfoValues values = questionService.getRandomQuestion(difficulty);

        return ResponseEntity.ok().body(values);
    }

    @PostMapping("/{encryptUuid}/hint")
    public ResponseEntity<?> getQuestionHint(@PathVariable("encryptUuid") String encryptUuid) {
        QuestionInfoValues values = questionService.getQuestionHint(encryptUuid);

        return ResponseEntity.ok().body(values);
    }

    @PostMapping("/{encryptUuid}/submit")
    public ResponseEntity<?> submitQuestionAnswer(@PathVariable("encryptUuid") String encryptUuid,
                                                  @RequestBody QuestionSubmitDto submitDto) {
        QuestionResultValues values = questionService.submitResponse(encryptUuid, submitDto.getResponseSongId());

        return ResponseEntity.ok().build();
    }


//    @GetMapping("/{encUuid}")
//    public ResponseEntity<?> getQuestionInstanceInfo() {
//
//    }



}
