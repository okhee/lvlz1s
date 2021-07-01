package kr.co.okheeokey.audiofile.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.co.okheeokey.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AudioFile {
    @Id @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    private Long difficulty = 0L;

    @ContentId
    private String contentId;

    @ContentLength
    private Long contentLength;

    @MimeType
    private String mimeType = "audio/flac";

    @Builder
    public AudioFile(Long difficulty, String mimeType) {
        this.difficulty = difficulty;
        this.mimeType = mimeType;
    }

    public void setQuestion(Question question) {
        if(this.question != null) {
            this.question.removeAudio(this.difficulty);
        }
        this.question = question;
        this.question.appendAudio(this.difficulty, this);
    }
}
