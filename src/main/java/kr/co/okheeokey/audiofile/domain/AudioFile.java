package kr.co.okheeokey.audiofile.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.co.okheeokey.question.domain.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AudioFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public AudioFile(Long difficulty) {
        this.difficulty = difficulty;
    }

    public void setQuestion(Question question) {
        if(this.question != null) {
            this.question.removeAudio(this.difficulty);
        }
        this.question = question;
        this.question.appendAudio(this.difficulty, this);
    }
}
