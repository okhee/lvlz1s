package kr.co.okheeokey.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizExistQueryValues;
import kr.co.okheeokey.quiz.vo.QuizStatusValues;
import kr.co.okheeokey.quizset.domain.QuizSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = QuizController.class)
public class QuizControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizService quizService;

    @MockBean private Quiz quiz;
    @MockBean private QuizSet quizSet;

    private final Long userId = 12L;
    private final Long quizSetId = 43L;
    private final Long songNum = 9L;
    private final Long quizId = 425L;

    @Test
    public void createQuiz_noPreviousQuiz_thenReturns201() throws Exception {
        // given
        QuizCreateDto dto = new QuizCreateDto(userId, quizSetId, songNum);

        when(quizService.previousQuiz(any(QuizExistQueryValues.class)))
                .thenReturn(Optional.empty());
        when(quizService.createNewQuiz(any(QuizCreateValues.class)))
                .thenReturn(quiz);
        when(quiz.getId()).thenReturn(quizId);

        // when
        mvc.perform(post("/quizs")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/quizs/" + quizId))
        .andDo(print());

        verify(quiz, times(1)).getId();
    }

    @Test
    public void createQuiz_previousQuizExists_thenReturns301() throws Exception {
        // given
        when(quizService.previousQuiz(any(QuizExistQueryValues.class)))
                .thenReturn(Optional.of(quiz));
        when(quiz.getId()).thenReturn(quizId);

        QuizCreateDto dto = new QuizCreateDto(userId, quizSetId, songNum);

        // when
        mvc.perform(post("/quizs")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
        // then
        .andExpect(status().isMovedPermanently())
        .andExpect(header().string("Location", "/quizs/" + quizId))
        .andDo(print());
    }
}
