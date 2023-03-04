package com.SYSC4806_Group13.SYSC4806_Project.Security;


import com.SYSC4806_Group13.SYSC4806_Project.AuthenticationSuperUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AuthenticationSuperUserUtil userUtil;

    @Test
    public void testProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/cartItems?userID=123"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testProtectedEndpointWithInValidAuthentication() throws Exception {
        userUtil.setSuperUserInRepo();
        String token = userUtil.getSuperUserToken();
        mockMvc.perform(get("/cartItems?userID=123").header("Authorization","Bearer "+"Sometoken"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void testProtectedEndpointWithValidAuthentication() throws Exception {
        userUtil.setSuperUserInRepo();
        String token = userUtil.getSuperUserToken();
        mockMvc.perform(get("/cartItems?userID=123").header("Authorization","Bearer "+token))
                .andExpect(status().is2xxSuccessful());
    }

}
