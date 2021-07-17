package kr.co.okheeokey.question.controller;

import kr.co.okheeokey.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

}
