package kr.co.okheeokey.question.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionInfoValues {
    private final String encryptUuid;
    private final Boolean hintAvailable;
    private final Long nextHintCost;

    @Builder
    public QuestionInfoValues(String encryptUuid, Boolean hintAvailable, Long nextHintCost) {
        this.encryptUuid = encryptUuid;
        this.hintAvailable = hintAvailable;
        this.nextHintCost = nextHintCost;
    }
}
