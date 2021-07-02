package kr.co.okheeokey.quiz.vo;

import kr.co.okheeokey.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionSubmitValues {
    private final User user;
    private final Long quizId;
    private final Long questionIndex;
    private final Long responseSongId;
}
