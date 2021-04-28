package kr.co.okheeokey.quizset.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuizSetControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void newQuizSet() throws Exception{
        String requestJson = "df";
        try {
            requestJson = new JSONObject()
                    .put("userId", 1L)
                    .put("title", "my quiz set 1")
                    .put("description", "my first quiz set..")
                    .put("songFileIdList", new JSONArray().put(1).put(2).put(3).put(4))
                    .toString();
        } catch (JSONException e){

        }
        System.out.println(requestJson);

        mvc.perform(post("/quizsets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isAccepted())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

}