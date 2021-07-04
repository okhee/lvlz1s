package kr.co.okheeokey.audiofile.controller;

import kr.co.okheeokey.audiofile.domain.AudioFileContentStore;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AudioFileControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private AudioFileContentStore audioFileContentStore;

    @Test
    @Transactional
    @WithMockUser(authorities = {"ADMIN"})
    public void setAudioFileTest() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "11001.mp3", "audio/mpeg",
                    new FileInputStream("src/main/resources/static/audio/11001.mp3"));
        Long questionId = 1L;
        Long difficulty = 0L;

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.multipart("/audiofiles?q=" + questionId + "&diff=" + difficulty)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andDo(print());

        String url = resultActions.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);

        // then
        Question question = repository.findById(questionId).orElseThrow(NoSuchElementException::new);
        assertArrayEquals(file1.getBytes(),
                IOUtils.toByteArray(audioFileContentStore.getContent(question.getAudioList().get(difficulty))));

        assert url != null;
        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(content().bytes(IOUtils.toByteArray(file1.getInputStream())));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = {"ADMIN"})
    public void modifyAudioFile() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "11001.mp3", "audio/mpeg",
                new FileInputStream("src/main/resources/static/audio/11001.mp3"));
        MockMultipartFile file2 = new MockMultipartFile("file", "11002.mp3", "audio/mpeg",
                new FileInputStream("src/main/resources/static/audio/11002.mp3"));
        Long questionId = 1L;
        Long difficulty = 0L;

        mvc.perform(MockMvcRequestBuilders.multipart("/audiofiles?q=" + questionId + "&diff=" + difficulty)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andDo(print());

        Question question = repository.findById(questionId).orElseThrow(NoSuchElementException::new);
        assertArrayEquals(file1.getBytes(),
                IOUtils.toByteArray(audioFileContentStore.getContent(question.getAudioList().get(difficulty))));

        // when
        mvc.perform(MockMvcRequestBuilders.multipart("/audiofiles?q=" + questionId + "&diff=" + difficulty + "&update=true")
                .file(file2)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        assertArrayEquals(file2.getBytes(),
                IOUtils.toByteArray(audioFileContentStore.getContent(question.getAudioList().get(difficulty))));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = {"ADMIN"})
    public void audioFileAlreadyExist() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "11001.mp3", "audio/mpeg",
                new FileInputStream("src/main/resources/static/audio/11001.mp3"));
        long questionId = 1L;
        long difficulty = 0L;

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.multipart("/audiofiles?q=" + questionId + "&diff=" + difficulty)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andDo(print());

        String url = resultActions.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);

        // when
        mvc.perform(MockMvcRequestBuilders.multipart("/audiofiles?q=" + questionId + "&diff=" + difficulty)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))

        // then
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message",
                        is("Audio file already exists in Question id { " + questionId + " }, difficulty { " + difficulty + " }; Use update request")))
                .andDo(print());


    }
}