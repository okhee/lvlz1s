package kr.co.okheeokey.quiz.exception;

import kr.co.okheeokey.quiz.controller.QuizController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@ControllerAdvice("kr.co.okheeokey.quiz")
public class QuizExceptionHandler {
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<?> questionOutOfBounds(Exception e) {
        return ResponseEntity.badRequest().body(EntityModel.of(e.getMessage()));
//                linkTo(methodOn(QuizController.class).submitQuestion())))

    }
}

