package kr.co.okheeokey.quizset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.okheeokey.auth.domain.JwtAuthTokenProvider;
import kr.co.okheeokey.auth.service.CustomUserDetailsService;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.quizset.service.QuizSetService;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockBean private QuizSetService quizSetService;
    @MockBean private QuizSet quizSet;
    @MockBean private CustomUserDetailsService customUserDetailsService;
    @MockBean private JwtAuthTokenProvider jwtAuthTokenProvider;

    @MockBean private User user;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    @Test
    public void createQuizSet_thenReturns201() throws Exception{
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