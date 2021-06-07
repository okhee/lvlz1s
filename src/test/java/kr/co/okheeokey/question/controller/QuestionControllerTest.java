package kr.co.okheeokey.question.controller;

import kr.co.okheeokey.audiofile.domain.AudioFileContentStore;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.question.service.QuestionService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private QuestionController questionController;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private AudioFileContentStore audioFileContentStore;

    @Test
    @Transactional
    public void setContent() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "11001.mp3", "audio/mpeg",
                    new FileInputStream("src/main/resources/static/audio/11001.mp3"));
        Long id = 1L;

        mvc.perform(MockMvcRequestBuilders.multipart("/questions/" + id)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/questions/1?diff=0"));

        Question question = repository.findById(id).orElseThrow(NoSuchElementException::new);
        assertArrayEquals(file1.getBytes(),
                IOUtils.toByteArray(audioFileContentStore.getContent(question.getAudioList().get(0L))));

        mvc.perform(MockMvcRequestBuilders.get("/questions/1?diff=0"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(IOUtils.toByteArray(file1.getInputStream())));
    }

    @Test
    @Transactional
    public void audioFileAlreadyExist() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "11001.mp3", "audio/mpeg",
                new FileInputStream("src/main/resources/static/audio/11001.mp3"));
        Long id = 1L;

        mvc.perform(MockMvcRequestBuilders.multipart("/questions/" + id)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/questions/1?diff=0"));

        mvc.perform(MockMvcRequestBuilders.multipart("/questions/" + id)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message",
                        is("Audio file already exists in Question id { " + id + " }, difficulty { " + 0 + " }; Use PUT request")));
    }
}