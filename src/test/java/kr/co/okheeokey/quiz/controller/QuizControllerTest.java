package kr.co.okheeokey.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.quiz.domain.Quiz;
import kr.co.okheeokey.quiz.dto.QuestionSubmitDto;
import kr.co.okheeokey.quiz.dto.QuizCreateDto;
import kr.co.okheeokey.quiz.service.QuizService;
import kr.co.okheeokey.quiz.vo.QuestionSubmitValues;
import kr.co.okheeokey.quiz.vo.QuizCreateValues;
import kr.co.okheeokey.quiz.vo.QuizStatusValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private QuizService quizService;

    @Mock private Quiz quiz;

    private final Long quizSetId = 43L;
    private final Long questionNum = 9L;
    private final Long quizId = 425L;

    @Test
    @WithMockUser
    public void createQuiz() throws Exception {
        // given
        when(quizService.createNewQuiz(any(QuizCreateValues.class)))
                .thenReturn(quiz);
        when(quiz.getId()).thenReturn(quizId);

        QuizCreateDto dto = new QuizCreateDto(quizSetId, questionNum);

        // when
        mvc.perform(post("/quiz")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .characterEncoding("UTF-8"))

        // then
        .andExpect(status().isAccepted())
        .andExpect(header().string("Location", "/quiz/" + quizId))
        .andDo(print());

        verify(quiz, times(1)).getId();
    }

    @Test
    @WithMockUser
    public void getQuizInfo() throws Exception {
        // given
        String title = "ttiit_";
        String description = "ddedi1";
        Boolean closed = true;

        Long questionNum = 6L;

        List<QuestionDifficulty> questionList = new ArrayList<>();
        for (int i = 0; i < questionNum; i++)
            questionList.add(QuestionDifficulty.MEDIUM);

        List<Long> responseList = new ArrayList<>();
        responseList.add(-1L);
        responseList.add(-1L);
        responseList.add(-1L);
        responseList.add(162L);
        responseList.add(-1L);
        responseList.add(-1L);

        List<Boolean> scoreList = new ArrayList<>();
        scoreList.add(false);
        scoreList.add(false);
        scoreList.add(false);
        scoreList.add(false);
        scoreList.add(true);
        scoreList.add(false);

        when(quizService.getQuizStatus(any(), anyLong())).thenReturn(
                new QuizStatusValues(title, description, closed, questionNum,
                        questionList, responseList, scoreList));

        // when
        mvc.perform(get("/quiz/" + quizId))

        // then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json"))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.closed", is(closed)))
                .andExpect(jsonPath("$.questionNum", is(questionNum.intValue())))

                .andExpect(jsonPath("$.questionList").isArray())

                .andExpect(jsonPath("$.responseList").isArray())
                .andExpect(jsonPath("$.responseList", hasSize(Math.toIntExact(questionNum))))
                .andExpect(jsonPath("$.responseList[0]", is(-1)))
                .andExpect(jsonPath("$.responseList[1]", is(-1)))
                .andExpect(jsonPath("$.responseList[3]", is(162)))
                .andExpect(jsonPath("$.scoreList[4]", is(true)))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void submitQuestion() throws Exception {
        // given
        Long quizId = 1625L;
        Long questionIndex = 515L;
        Long responseSongId = 72L;

        QuestionSubmitDto dto = new QuestionSubmitDto(responseSongId);

        when(quizService.saveQuestionResponse(any(QuestionSubmitValues.class)))
                .thenReturn(new Quiz());

        // when
        mvc.perform(post("/quiz/{id}/q/{qid}", quizId, questionIndex)
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))

        //then
        .andExpect(status().isAccepted());
    }
}
