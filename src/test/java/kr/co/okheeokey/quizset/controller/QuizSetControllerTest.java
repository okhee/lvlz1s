package kr.co.okheeokey.quizset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.okheeokey.auth.domain.JwtAuthTokenProvider;
import kr.co.okheeokey.auth.service.CustomUserDetailsService;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.quizset.vo.QuizSetInfoValues;
import kr.co.okheeokey.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuizSetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private QuizSetService quizSetService;
    @MockBean private QuizSet quizSet;
    @MockBean private CustomUserDetailsService customUserDetailsService;
    @MockBean private JwtAuthTokenProvider jwtAuthTokenProvider;

    @MockBean private User user;

    @Test
    @WithMockUser
    public void getAllQuizSet() throws Exception {
        // given
        long id = 14L; String title = "125"; String description = "hse3";
        boolean readyMade = false; long questionPoolSize = 15L;
        double averageDifficulty = 17.1;

        QuizSetInfoValues values1 = QuizSetInfoValues.builder().id(id)
                .title(title)
                .description(description)
                .readyMade(readyMade)
                .questionPoolSize(questionPoolSize)
                .averageDifficulty(averageDifficulty)
                .build();

        id = 151L; title = "j4w5"; description = "ns541";
        readyMade = true; questionPoolSize = 75L;
        averageDifficulty = 198.457;

        QuizSetInfoValues values2 = QuizSetInfoValues.builder().id(id)
                .title(title)
                .description(description)
                .readyMade(readyMade)
                .questionPoolSize(questionPoolSize)
                .averageDifficulty(averageDifficulty)
                .build();

        List<QuizSetInfoValues> valuesList = new ArrayList<>();
        valuesList.add(values1);
        valuesList.add(values2);

        when(quizSetService.getAllQuizSet(any()))
                .thenReturn(valuesList);

        // when
        mvc.perform(get("/quizsets"))

        // then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

                .andExpect(jsonPath("$[0].id", is(values1.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(values1.getTitle())))
                .andExpect(jsonPath("$[0].description", is(values1.getDescription())))
                .andExpect(jsonPath("$[0].readyMade", is(values1.getReadyMade())))
                .andExpect(jsonPath("$[0].questionPoolSize", is(values1.getQuestionPoolSize().intValue())))
                .andExpect(jsonPath("$[0].averageDifficulty", is(values1.getAverageDifficulty())))

                .andExpect(jsonPath("$[1].id", is(values2.getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(values2.getTitle())))
                .andExpect(jsonPath("$[1].description", is(values2.getDescription())))
                .andExpect(jsonPath("$[1].readyMade", is(values2.getReadyMade())))
                .andExpect(jsonPath("$[1].questionPoolSize", is(values2.getQuestionPoolSize().intValue())))
                .andExpect(jsonPath("$[1].averageDifficulty", is(values2.getAverageDifficulty())))

                .andDo(print());
    }

    @Test
    @WithMockUser
    public void createQuizSet_thenReturns201() throws Exception {
        // given
        when(jwtAuthTokenProvider.getSubject(anyString())).thenReturn("nname");
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(user);

        Long userId = 41L;
        String title = "This is ttttitle";
        String description = "thiS is descripttion";

        List<Long> albumIdList = Collections.singletonList(142L);
        List<Long> songIdList = Arrays.asList(1L, 2L, 3L, 4L);

        Long createdQuizSetId = 51L;

        QuizSetAddDto dto = new QuizSetAddDto(title, description, albumIdList, songIdList,
                true, true, true);

        doReturn(quizSet).when(quizSetService).createNewQuizSet(any(User.class), any(QuizSetCreateValues.class));
        when(quizSet.getId())
                .thenReturn(createdQuizSetId);

        // when
        mvc.perform(post("/quizsets")
                .header("Authorization", "Bearer lalala")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(header().stringValues("Location", "/quizsets/" + createdQuizSetId))
        .andDo(print());
    }

    @Test
    @WithMockUser
    public void createQuizSet_withInvalidQuestionId_thenThrowsException() throws Exception {
        // given
        when(jwtAuthTokenProvider.getSubject(anyString())).thenReturn("nname");
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(user);

        doThrow(new IllegalArgumentException()).when(quizSetService).createNewQuizSet(any(User.class), any(QuizSetCreateValues.class));

        // when
        mvc.perform(post("/quizsets")
                .header("Authorization", "Bearer lalala")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new QuizSetAddDto("", "", Collections.emptyList(), Collections.emptyList(),
                        true, true, true)))
        )
        // then
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

}