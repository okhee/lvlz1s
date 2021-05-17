package kr.co.okheeokey.songfile.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "SONG_FILE_ID")
    private SongFile songFile;

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

    public void setSongFile(SongFile songFile) {
        if(this.songFile != null) {
            this.songFile.removeAudio(this.difficulty);
        }
        this.songFile = songFile;
        this.songFile.appendAudio(this.difficulty, this);
    }
}
