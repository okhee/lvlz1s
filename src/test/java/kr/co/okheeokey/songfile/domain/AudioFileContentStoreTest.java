package kr.co.okheeokey.songfile.domain;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AudioFileContentStoreTest {
    @Autowired
    private AudioFileContentStore contentStore;

    @Autowired
    private AudioFileRepository repository;

    @Test
    public void tt() throws Exception{
        repository.save(new AudioFile());

        AudioFile audioFile = repository.findById(1L).orElseThrow(NoSuchElementException::new);

        contentStore.setContent(audioFile,
                new FileInputStream("src/main/resources/static/audio/11001.mp3"));

        assertArrayEquals(IOUtils.toByteArray(new FileInputStream("src/main/resources/static/audio/11001.mp3")),
                        IOUtils.toByteArray(contentStore.getContent(audioFile)));
    }
}