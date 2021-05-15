package kr.co.okheeokey.quiz.controller;

import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.dto.QuizResultDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizExistQueryValues;
import kr.co.okheeokey.quiz.vo.QuizResultValues;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.song.domain.SongFile;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizs")
public class QuizController {
    private final QuizSetService quizSetService;
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDto dto) {
        try {
            quizSetService.isAllowed(dto.getUserId(), dto.getQuizSetId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        Quiz quiz = quizService.previousQuiz(new QuizExistQueryValues(dto.getUserId(), dto.getQuizSetId()))
                .orElseGet(() -> quizService.createNewQuiz(new QuizCreateValues(dto)));

        URI uri = URI.create("/quizs/" + quiz.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public EntityModel<?> resumeQuiz(@PathVariable("id") Long quizId,
                                                       @RequestParam(value = "q", required = false, defaultValue = "1") Long questionId){
        SongFile question = quizService.getQuestion(quizId, questionId);

        return EntityModel.of(Collections.singletonMap("filename", question.getSongFileName()),
                linkTo(methodOn(QuizController.class).resumeQuiz(quizId, questionId)).withSelfRel(),
                linkTo(methodOn(QuizController.class).submitQuestion(quizId, questionId, null)).withRel("submit"));
    }

    @GetMapping("/{id}/submit")
    public EntityModel<?> submitQuiz(@PathVariable("id") Long quizId) {
        quizService.closeQuiz(quizId);
        return EntityModel.of(linkTo(methodOn(QuizController.class).quizResult(quizId)).withRel(IanaLinkRelations.NEXT));
    }

    @GetMapping(value = "/{id}/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<?, ?> quizResult(@PathVariable("id") Long quizId) {
        QuizResultValues values = quizService.getQuizResult(quizId);
//        return EntityModel.of(QuizResultDto.of(values),
//                linkTo(methodOn(QuizController.class).quizResult(quizId)).withSelfRel());

//        JSONObject responseJson = new JSONObject()
//                .put("userId", values.getQuizId().toString())
//                .put("quizSetTitle", values.getQuizSetTitle().toString())
//                .put("description", "my first quiz set..")
//                .put("songFileIdList", new JSONArray().put(1).put(2).put(3).put(4))
//                .toString();

        return values.getScoreList();
//        return ResponseEntity.ok().body(QuizResultDto.of(values));
//        return QuizResultDto.of(values);
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
