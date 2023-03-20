package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class CoverRestControllerTest {
    @Autowired
    private CoverRestController restController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        assertThat(restController).isNotNull();
    }

    @Test
    public void fails() throws Exception {
        mockMvc.perform(post("/covers/1"))
                .andExpect(status().is4xxClientError());
    }
}
