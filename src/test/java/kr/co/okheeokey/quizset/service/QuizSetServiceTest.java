package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.vo.QuizSetInfoValues;
import kr.co.okheeokey.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QuizSetServiceTest {
    @InjectMocks
    public QuizSetService quizSetService;

    @Mock
    public QuizSetRepository quizSetRepository;

    @Test
    public void getAllQuizSet() {
        // given
        QuizSet readyMadeQuizSet = QuizSet.builder()
                .owner(new User())
                .title("125")
                .description("hse3")
                .readyMade(true)
                .questionPool(Collections.singletonList(new Question()))
                .build();

        QuizSet userMadeQuizSet = QuizSet.builder()
                .owner(new User())
                .title("1gf1g")
                .description("42q3")
                .readyMade(false)
                .questionPool(Collections.singletonList(new Question()))
                .build();

        when(quizSetRepository.findAllByReadyMadeIsTrue())
                .thenReturn(new ArrayList<>(Collections.singletonList(readyMadeQuizSet)));
        when(quizSetRepository.findAllByOwner(any(User.class)))
                .thenReturn(new ArrayList<>(Collections.singletonList(userMadeQuizSet)));

        // when
        List<QuizSetInfoValues> valuesList = quizSetService.getAllQuizSet(new User());

        // then
        assertEquals(readyMadeQuizSet.getTitle(), valuesList.get(0).getTitle());
        assertEquals(readyMadeQuizSet.getDescription(), valuesList.get(0).getDescription());

        assertEquals(userMadeQuizSet.getTitle(), valuesList.get(1).getTitle());
        assertEquals(userMadeQuizSet.getDescription(), valuesList.get(1).getDescription());
    }
}
