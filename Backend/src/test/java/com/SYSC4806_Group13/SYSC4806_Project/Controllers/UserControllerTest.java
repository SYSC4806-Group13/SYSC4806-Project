package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.UserRepository;
import com.SYSC4806_Group13.SYSC4806_Project.AuthenticationSuperUserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AuthenticationSuperUserUtil userUtils;
    @Autowired
    UserRepository userRepository;

    @Test
    public void testUserBecomesASeller() throws Exception {
        userUtils.setSuperUserInRepo();
        String token = userUtils.getSuperUserToken();

        Assert.isTrue(!userRepository.findUserById(1L).getIsSeller(),"The user should not be a seller");

        mockMvc.perform(patch("/users/becomeSeller")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().is2xxSuccessful());
        Assert.isTrue(userRepository.findUserById(1L).getIsSeller(),"The user should be a seller");
    }
}
