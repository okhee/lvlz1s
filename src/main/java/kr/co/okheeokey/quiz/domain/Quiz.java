package kr.co.okheeokey.quiz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.audiofile.exception.NoAudioFileExistsException;
import kr.co.okheeokey.common.domain.TimeStampEntity;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.util.CryptoUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "quiz")
@SequenceGenerator(name = "QUIZ_SEQ_GENERATOR",
                    sequenceName = "QUIZ_SEQ",
                    initialValue = 1, allocationSize = 1)
public class Quiz extends TimeStampEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
                        generator = "QUIZ_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "quiz_set_id")
    private QuizSet quizSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany
    @JsonManagedReference
    private List<Question> questionList = new ArrayList<>();

    @ElementCollection
    private final Map<Long, Long> responseMap = new HashMap<>();

    @ElementCollection
    private final Map<Long, Boolean> scoreList = new HashMap<>();

    // Todo: make hintMap thread-safe
    @ElementCollection
    private final Map<Long, Long> hintMap = new HashMap<>();

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT '0'")
    private Long hintTokenUsed = 0L;

    @Column(nullable = false)
    private Long questionNum;

    @Column(nullable = false)
    private Boolean closed;

    @Builder
    public Quiz(QuizSet quizSet, User owner, List<Question> questionList, Long questionNum, Boolean closed) {
        this.quizSet = quizSet;
        this.owner = owner;
        this.questionList = questionList;
        this.questionNum = questionNum;
        this.closed = closed;

        for (int i = 0; i < this.questionNum; i++)
            this.hintMap.put((long) i, 0L);
    }

    public String getAudioFileUuid(Long questionIndex) throws IndexOutOfBoundsException, NoAudioFileExistsException {
        questionIndex -= 1L;

        Question question = this.getQuestionList().get(questionIndex.intValue());

        Long finalQuestionIndex = questionIndex;
        AudioFile audioFile = Optional.ofNullable(question.getAudioList().get(hintMap.get(questionIndex)))
                .orElseThrow(() -> new NoAudioFileExistsException(question.getId(), hintMap.get(finalQuestionIndex)));

        return CryptoUtils.encryptUuid(audioFile.getUuid());
    }

    // check if additional hint is available and return its cost
    // if not, return -1
    public Long isHintAvailable(Long questionIndex) throws IndexOutOfBoundsException {
        questionIndex -= 1L;

        Question question = this.getQuestionList().get(questionIndex.intValue());
        Long hintIndex = hintMap.get(questionIndex) + 1;
        if (question.getAudioList().containsKey(hintIndex))
            return hintCost(hintIndex);
        else
            return -1L;
    }

    public void getHint(Long questionIndex) throws IndexOutOfBoundsException {
        questionIndex -= 1L;

        Question question = this.getQuestionList().get(questionIndex.intValue());
        Long hintIndex = hintMap.get(questionIndex) + 1;
        if (question.getAudioList().containsKey(hintIndex)){
            hintMap.put(questionIndex, hintIndex);
            hintTokenUsed += hintCost(hintIndex);
        }
    }

    public void saveResponse(Long questionIndex, Song response) {
        responseMap.put(questionIndex - 1L, response.getId());
    }

    public void scoreResponse() {
        for (int i = 0; i < this.questionList.size(); i++){
            Boolean isAnswer = this.questionList.get(i).getSong().getId().equals(this.responseMap.get((long) i));
            this.scoreList.put((long) i, isAnswer);
        }
    }

    public void close() {
        this.closed = true;
    }

    public Long hintCost(Long hintIndex) {
        return 2 * hintIndex;
    }
}
