package kr.co.okheeokey.quiz.controller;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.*;
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
    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDto dto) throws NoSuchElementException, IllegalAccessException {
        return quizService.previousQuiz(new QuizExistQueryValues(dto.getUserId(), dto.getQuizSetId()))
            .map(q -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .location(URI.create("/quizs/" + q.getId())).build())
            .orElseGet(() ->
                ResponseEntity.created(
                    URI.create("/quizs/" + quizService.createNewQuiz(new QuizCreateValues(dto)).getId())
                ).build()
            );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizInfo(@PathVariable("id") Long quizId) {
        QuizStatusValues quizStatus = quizService.getQuizStatus(quizId);

        return ResponseEntity.ok().body(quizStatus);
    }

    @GetMapping("/{id}/q/{qid}")
    public ResponseEntity<?> getQuestion(@PathVariable("id") Long quizId, @PathVariable("qid") Long questionId)
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
                                            @RequestBody QuestionSubmitDto submitDto) throws NoSuchElementException, IndexOutOfBoundsException {
        quizService.saveQuestionResponse(new QuestionSubmitValues(quizId, questionId, submitDto.getResponseSongId()));

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).getQuestion(quizId, questionId+1)).withRel(IanaLinkRelations.NEXT))
        );
    }

    @PostMapping("/{id}")
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

    // Todo: check user authorization
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> giveUpQuiz(@PathVariable("id") Long quizId) throws NoSuchElementException {
        quizService.deleteQuiz(quizId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
