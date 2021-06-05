package kr.co.okheeokey.quizset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = QuizSetController.class)
public class QuizSetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizSetService quizSetService;

    @MockBean
    private QuizSet quizSet;

    @Test
    public void createQuizSet_thenReturns201() throws Exception{
        // given
        Long userId = 41L;
        String title = "This is ttttitle";
        String description = "thiS is descripttion";
        List<Long> songFileIdList = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));

        Long createdQuizSetId = 51L;

        QuizSetAddDto dto = new QuizSetAddDto(userId, title, description, songFileIdList);

        doReturn(quizSet).when(quizSetService).createNewQuizSet(any(QuizSetCreateValues.class));
        when(quizSet.getId())
                .thenReturn(createdQuizSetId);

        // when
        mvc.perform(post("/quizsets")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(header().stringValues("Location", "/quizsets/" + createdQuizSetId))
        .andDo(print());
    }

    }

}