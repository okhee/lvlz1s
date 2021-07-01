package kr.co.okheeokey.quiz.controller;

import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.*;
import kr.co.okheeokey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> createQuiz(@AuthenticationPrincipal User user,
            @RequestBody QuizCreateDto dto)
            throws NoSuchElementException, IllegalAccessException {
        return quizService.previousQuiz(new QuizExistQueryValues(user, dto.getQuizSetId()))
            .map(q -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create("/quizs/" + q.getId())).build())
            .orElseGet(() -> ResponseEntity
                    .created(URI.create("/quizs/" + quizService.createNewQuiz(new QuizCreateValues(user, dto)).getId())).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizInfo(@AuthenticationPrincipal User user,
            @PathVariable("id") Long quizId) throws NoSuchElementException, IllegalAccessException {
        QuizStatusValues quizStatus = quizService.getQuizStatus(user, quizId);

        return ResponseEntity.ok().body(
                EntityModel.of(quizStatus,
                        linkTo(methodOn(QuizController.class).getQuizInfo(user, quizId)).withSelfRel())
        );
    }

    @GetMapping("/{id}/q/{qid}")
    public ResponseEntity<?> getQuestion(@AuthenticationPrincipal User user,
            @PathVariable("id") Long quizId, @PathVariable("qid") Long questionIndex)
            throws IndexOutOfBoundsException, NoSuchElementException, IllegalAccessException {
        Question question = quizService.getQuestion(user, quizId, questionIndex);

        return ResponseEntity.ok().body(
                EntityModel.of(Collections.singletonMap("filename", question.getQuestionName()),
                linkTo(methodOn(QuizController.class).getQuestion(user, quizId, questionIndex)).withSelfRel(),
                linkTo(methodOn(QuizController.class).submitQuestion(user, quizId, questionIndex, null)).withRel("submit"))
        );
    }

    @PostMapping("/{id}/q/{qid}")
    public ResponseEntity<?> submitQuestion(@AuthenticationPrincipal User user, @PathVariable("id") Long quizId,
            @PathVariable("qid") Long questionIndex, @RequestBody QuestionSubmitDto submitDto)
            throws NoSuchElementException, IndexOutOfBoundsException, IllegalAccessException {
        quizService.saveQuestionResponse(new QuestionSubmitValues(user, quizId, questionIndex, submitDto.getResponseSongId()));

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).getQuestion(user, quizId, questionIndex+1)).withRel(IanaLinkRelations.NEXT))
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> submitQuiz(@AuthenticationPrincipal User user, @PathVariable("id") Long quizId)
            throws NoSuchElementException, IllegalAccessException {
        quizService.closeQuiz(user, quizId);

        return ResponseEntity.accepted().body(
            EntityModel.of(linkTo(methodOn(QuizController.class).getQuizInfo(user, quizId)).withRel(IanaLinkRelations.NEXT))
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> giveUpQuiz(@AuthenticationPrincipal User user, @PathVariable("id") Long quizId)
            throws NoSuchElementException, IllegalAccessException {
        quizService.deleteQuiz(user, quizId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
