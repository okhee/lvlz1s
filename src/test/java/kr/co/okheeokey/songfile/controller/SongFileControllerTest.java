package kr.co.okheeokey.songfile.controller;

import kr.co.okheeokey.songfile.domain.AudioFileContentStore;
import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.songfile.domain.SongFileRepository;
import kr.co.okheeokey.songfile.service.SongFileService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SongFileControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SongFileController songFileController;

    @Autowired
    private SongFileService songFileService;

    @Autowired
    private SongFileRepository repository;

    @Autowired
    private AudioFileContentStore audioFileContentStore;

    @Test
    @Transactional
    public void setContent() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("youre_not_alone_1m", "11001.mp3", "multipart/form-data",
                    new FileInputStream(new File("src/main/resources/static/audio/11001.mp3")));
        MockMultipartFile file2 = new MockMultipartFile("can_you_hear_me", "11002.mp3", "multipart/form-data",
                    new FileInputStream(new File("src/main/resources/static/audio/11002.mp3")));

        Long id = 1L;

        MultipartFile[] files = {file1, file2};
        songFileController.setContent(id, files);

//        mvc.perform(MockMvcRequestBuilders.multipart("/songfiles/" + id)
//                    .file(file1)
//                    .file(file2)
//                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                    .andExpect(status().isOk());

        SongFile songFile = repository.findById(id).get();
        assertArrayEquals(file1.getBytes(),
                IOUtils.toByteArray(audioFileContentStore.getContent(songFile.getAudioList().get(0L))));
        assertArrayEquals(file2.getBytes(),
                IOUtils.toByteArray(audioFileContentStore.getContent(songFile.getAudioList().get(1L))));

//        mvc.perform(MockMvcRequestBuilders.get("/songfiles/1?diff=0"))
//                .andExpect(status().isOk())
//                .andExpect(content().bytes(IOUtils.toByteArray(file1.getInputStream())));

        assertArrayEquals(IOUtils.toByteArray(file1.getInputStream()), songFileService.getAudioFile(1L, 0L));

        ResponseEntity<byte[]> responseEntity = songFileController.getAudioFile(id, 0L);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertArrayEquals(IOUtils.toByteArray(file1.getInputStream()), responseEntity.getBody());

    }
}