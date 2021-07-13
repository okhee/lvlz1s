package kr.co.okheeokey.song;

import kr.co.okheeokey.song.domain.*;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SongTest {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public SongTest() {
    }

    @Test
    public void name() {
        Song s1 = Song.builder()
                .songName("Candy Jelly Love")
                .build();
        Song s2 = Song.builder()
                .songName("Hi~")
                .build();

        Question file1 = new Question();
        Question file2 = new Question();

        Album sample_album = Album.builder()
                .albumName("Girls' Invasion")
                .releasedDate(LocalDate.of(2014, 11, 17))
                .albumCover("tlksjdfoi.jpg")
                .build();

        file1.setSong(s1);
        file2.setSong(s1);

        s1.setAlbum(sample_album);
        s2.setAlbum(sample_album);

    }
}