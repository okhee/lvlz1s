package kr.co.okheeokey.songfile.domain;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AudioFileContentStoreTest {
    @Autowired
    private AudioFileContentStore contentStore;

    @Autowired
    private AudioFileRepository repository;

    @Test
    public void saveAudioFileToContentStore() throws Exception{
        // given
        AudioFile audioFile = new AudioFile();

        repository.save(audioFile);
        Long fid = audioFile.getId();

        // when
        AudioFile returnAudioFile = repository.findById(fid).orElseThrow(NoSuchElementException::new);
        contentStore.setContent(returnAudioFile,
                new FileInputStream("src/main/resources/static/audio/11001.mp3"));

        // then
        assertEquals(audioFile, returnAudioFile);
        assertArrayEquals(IOUtils.toByteArray(new FileInputStream("src/main/resources/static/audio/11001.mp3")),
                        IOUtils.toByteArray(contentStore.getContent(returnAudioFile)));
    }
}