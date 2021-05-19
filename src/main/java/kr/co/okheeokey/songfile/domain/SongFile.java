package kr.co.okheeokey.songfile.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.songfile.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.songfile.exception.NoAudioFileExistsException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
@Getter
public class SongFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "SONG_ID")
    private Song song;

    // TODO: change to 'song information'
    @Column(nullable = false)
    private String songFileName;

    // key(Long): difficulty (length of audio file)
    // value(AudioFile): audioFile
    @OneToMany(mappedBy = "songFile")
    @JsonManagedReference
    private Map<Long, AudioFile> audioList = new HashMap<>();

    @Builder
    public SongFile(String songFileName) {
        this.songFileName = songFileName;
    }

    public void diffEmptyCheck(Long difficulty) throws AudioFileAlreadyExistsException {
        if (this.audioList.containsKey(difficulty)) {
            throw new AudioFileAlreadyExistsException("Audio file already exists in SongFile id { " + id
                                                    + " }, difficulty { " + difficulty + " }; Use PUT request");
        }
    }

    public void diffExistCheck(Long difficulty) throws NoAudioFileExistsException {
        if (!this.audioList.containsKey(difficulty)) {
            throw new NoAudioFileExistsException("Audio file does not exists in SongFile id { " + id
                                        + " }, difficulty { " + difficulty + " }; Add audio file first");
        }
    }

    public void setSong(Song song) {
        if(this.song != null){
            this.song.getSongFile().remove(this);
        }
        this.song = song;
        song.getSongFile().add(this);
    }

    public AudioFile getAudio(Long difficulty) throws NoAudioFileExistsException{
        if (!this.audioList.containsKey(difficulty)) {
            throw new NoAudioFileExistsException("No audio file exists in SongFile id { " + id
                                                + " }, difficulty { " + difficulty + " }");
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

