package kr.co.okheeokey.quizset.controller;

import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController("/quizsets")
public class QuizSetController {
    private final QuizSetService quizSetService;

    @GetMapping("/")
    public List<QuizSet> quizSetList() {
        return quizSetService.getAllQuizSet();
    }

//    @GetMapping("/new")
//    public

    @PostMapping("/")
    public ResponseEntity<Void> newQuizSet(@RequestBody QuizSetAddDto quizSetAddDto, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");

        String title = QuizSetAddDto.getTitle();

        quizSetService.addQuizSet();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void>



}
