package com.snippr.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SnipprApiApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void listAll() throws Exception {
        mvc.perform(get("/snippets"))
                .andExpect(status().isOk());
    }

    @Test
    void createAndFetch() throws Exception {
        String body = "{\"lang\":\"python\", \"code\":\"print('z')\"}";
        mvc.perform(post("/snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/snippets/")));

        mvc.perform(get("/snippets?lang=python"))
                .andExpect(status().isOk());
    }
}
