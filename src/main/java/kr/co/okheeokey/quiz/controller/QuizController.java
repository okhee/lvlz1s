package kr.co.okheeokey.quiz.controller;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizExistQueryValues;
import kr.co.okheeokey.quiz.vo.QuizResultValues;
import kr.co.okheeokey.songfile.domain.SongFile;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/quizs")
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDto dto) throws Exception {
        Quiz quiz = quizService.previousQuiz(new QuizExistQueryValues(dto.getUserId(), dto.getQuizSetId()))
                .orElseGet(() -> quizService.createNewQuiz(new QuizCreateValues(dto)));

        return ResponseEntity.created(URI.create("/quizs/" + quiz.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable("id") Long quizId,
                                      @RequestParam(value = "q", required = false, defaultValue = "1") Long questionId)
                                        throws IndexOutOfBoundsException, NoSuchElementException {
        SongFile question = quizService.getQuestion(quizId, questionId);

        return ResponseEntity.ok().body(
                EntityModel.of(Collections.singletonMap("filename", question.getSongFileName()),
                linkTo(methodOn(QuizController.class).getQuestion(quizId, questionId)).withSelfRel(),
                linkTo(methodOn(QuizController.class).submitQuestion(quizId, questionId, null)).withRel("submit"))
        );
    }

    @PostMapping("/{id}/q/{qid}")
    public ResponseEntity<?> submitQuestion(@PathVariable("id") Long quizId, @PathVariable("qid") Long questionId,
                                         @RequestBody QuestionSubmitDto submitDto) throws NoSuchElementException {
        quizService.saveQuestionResponse(new QuestionSubmitValues(quizId, questionId, submitDto.getResponseSongId()));

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).getQuestion(quizId, questionId+1)).withRel(IanaLinkRelations.NEXT))
        );
    }

    @GetMapping("/{id}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable("id") Long quizId) throws NoSuchElementException {
        quizService.closeQuiz(quizId);

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).quizResult(quizId)).withRel(IanaLinkRelations.NEXT))
        );
    }

    @GetMapping(value = "/{id}/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> quizResult(@PathVariable("id") Long quizId) throws NoSuchElementException{
        QuizResultValues values = quizService.getQuizResult(quizId);

        return ResponseEntity.ok().body(
                EntityModel.of(values,
                        linkTo(methodOn(QuizController.class).quizResult(quizId)).withSelfRel())
        );
    }

    @PostMapping("/{id}/q/{qid}")
    public EntityModel<?> submitQuestion(@PathVariable("id") Long quizId, @PathVariable("qid") Long questionId,
                                         @RequestBody QuestionSubmitDto submitDto) {
        quizService.saveQuestionResponse(new QuestionSubmitValues(quizId, questionId, submitDto.getResponseSongId()));

        return EntityModel.of(linkTo(methodOn(QuizController.class).resumeQuiz(quizId, questionId+1)).withRel(IanaLinkRelations.NEXT));
    }
//
//    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> dslofi() {
//
//    }

}
