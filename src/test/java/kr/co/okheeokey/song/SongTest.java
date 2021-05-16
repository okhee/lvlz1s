package kr.co.okheeokey.song;

import kr.co.okheeokey.song.domain.*;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.songfile.domain.SongFileRepository;
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
    private SongHashRepository songHashRepository;
    @Autowired
    private SongFileRepository songFileRepository;

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

        SongHash sh1 = SongHash.builder()
                .songHash("Candy!!")
                .build();
        SongHash sh2 = SongHash.builder()
                .songHash("Jelly!!")
                .build();
        SongHash sh3 = SongHash.builder()
                .songHash("Hello~!")
                .build();

        SongFile file1 = SongFile.builder()
                .songFileName("02_0807_Candy_jelly_love.mp3")
                .build();

        SongFile file2 = SongFile.builder()
                .songFileName("02_0181_Candy_jelly_love.mp3")
                .build();

        Album sample_album = Album.builder()
                .albumName("Girls' Invasion")
                .releasedDate(LocalDate.of(2014, 11, 17))
                .albumCover("tlksjdfoi.jpg")
                .build();

        sh1.setSong(s1);
        sh2.setSong(s1);

        sh3.setSong(s2);

        file1.setSong(s1);
        file2.setSong(s1);

        s1.setAlbum(sample_album);
        s2.setAlbum(sample_album);

    }
}