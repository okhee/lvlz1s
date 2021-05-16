package kr.co.okheeokey.songfile.controller;

import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.songfile.domain.SongFileRepository;
import kr.co.okheeokey.songfile.service.SongFileService;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@NoArgsConstructor
public class SongFileControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SongFileService songFileService;

    @Autowired
    private SongFileRepository repository;

    @Test
    public void setContent() throws Exception {
        FileInputStream audioStream = new FileInputStream(new File("src/main/resources/static/audio/11001.mp3"));
        MockMultipartFile file = new MockMultipartFile("file", "11001.mp3", "multipart/form-data",
                                    audioStream);
        Long id = 1L;

        mvc.perform(MockMvcRequestBuilders.multipart("/songfiles/" + id)
                    .file(file))
                    .andExpect(status().isOk());

        SongFile songFile = repository.findById(id).get();
        assertArrayEquals(file.getBytes(), songFile.getAudio());
    }
}