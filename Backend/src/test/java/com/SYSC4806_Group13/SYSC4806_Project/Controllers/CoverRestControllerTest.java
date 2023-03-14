package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class CoverRestControllerTest {
    @Autowired
    private CoverRestController restController;

    @Test
    public void contextLoads() {
        assertThat(restController).isNotNull();
    }


}
