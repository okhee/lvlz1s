package kr.co.okheeokey.question.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.audiofile.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.audiofile.exception.NoAudioFileExistsException;
import kr.co.okheeokey.song.domain.Song;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "question")
@SequenceGenerator(name = "QUESTION_SEQ_GENERATOR",
                    sequenceName = "QUESTION_SEQ",
                    initialValue = 1, allocationSize = 1)
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "song_id")
    private Song song;

    private Long answerLocationInSecond;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(10) default 'EASY'")
    private QuestionDifficulty difficulty = QuestionDifficulty.EASY;

    private String questionInfo;

    // key(Long): stage; length of audio file
    // value(AudioFile): audioFile
    @OneToMany(mappedBy = "question")
    @JsonManagedReference
    private final Map<Long, AudioFile> audioList = new HashMap<>();

    public void diffEmptyCheck(Long difficulty) throws AudioFileAlreadyExistsException {
        if (this.audioList.containsKey(difficulty)) {
            throw new AudioFileAlreadyExistsException(this.getId(), difficulty);
        }
    }

    public void diffExistCheck(Long difficulty) throws NoAudioFileExistsException {
        if (!this.audioList.containsKey(difficulty)) {
            throw new NoAudioFileExistsException(this.getId(), difficulty);
        }
    }

    public void setSong(Song song) {
        if(this.song != null){
            this.song.getQuestion().remove(this);
        }
        this.song = song;
        song.getQuestion().add(this);
    }

    public AudioFile getAudio(Long difficulty) throws NoAudioFileExistsException {
        if (!this.audioList.containsKey(difficulty)) {
            throw new NoAudioFileExistsException(this.getId(), difficulty);
        }
        return this.audioList.get(difficulty);
    }

    public void appendAudio(Long difficulty, AudioFile audio) {
        this.audioList.put(difficulty, audio);
    }

    public void removeAudio(Long difficulty) {
        this.audioList.remove(difficulty);
    }
}

