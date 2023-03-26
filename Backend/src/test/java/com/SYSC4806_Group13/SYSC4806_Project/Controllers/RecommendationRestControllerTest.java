package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.*;
import org.springframework.util.Assert;
import com.SYSC4806_Group13.SYSC4806_Project.AuthenticationSuperUserUtil;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.*;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.*;
import com.SYSC4806_Group13.SYSC4806_Project.Security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecommendationRestControllerTest {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    AuthenticationSuperUserUtil usersUtil;

    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    public void beforeTest() {
        //Add the SuperUser and token
        usersUtil.setSuperUserInRepo();
        this.token = usersUtil.getSuperUserToken();
    }

    @AfterEach
    public void resetTest() {
        // Reset the database to prevent tests from affecting each other
        cartItemRepository.deleteAll();
        listingRepository.deleteAll();
    }

    @Test
    void recommendation_test() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // create user
        User user1 = new User();
        User user2 = new User();
        userRepository.save(user1);
        userRepository.save(user2);

        // get user id
        Long userId = user1.getId();
        Long userId2 = user2.getId();

        // create listings
        Listing listing1 = new Listing(userId, "1", "1", 0.0F, "1", "1", "1", 5, "1", true);
        Listing listing2 = new Listing(userId, "2", "2", 0.0F, "2", "2", "2", 5, "2", true);
        listingRepository.save(listing1);
        listingRepository.save(listing2);

        // create checked out cart items
        CartItem cartItem1 = new CartItem(userId, listing1, 1L);
        CartItem cartItem2 = new CartItem(userId, listing2, 1L);
        CartItem cartItem3 = new CartItem(userId2, listing1, 1L);
        cartItem1.checkout();
        cartItem2.checkout();
        cartItem3.checkout();
        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);
        cartItemRepository.save(cartItem3);

        // get recommendations
        MvcResult result = mockMvc.perform(get("/recommendation/" + userId2)
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();
        Set recommendations = mapper.readValue(result.getResponse().getContentAsByteArray(), Set.class);
        Assert.isTrue(recommendations.size() == 1, "Recommendation list should have size 1");

        // delete users
        userRepository.deleteById(userId);
        userRepository.deleteById(userId2);
    }
}
