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

    private ObjectMapper mapper;
    private User user1;
    private User user2;
    private Long user1Id;
    private Long user2Id;
    private Listing listing1;
    private Listing listing2;

    @BeforeEach
    public void beforeTest() {
        //Add the SuperUser and token
        this.usersUtil.setSuperUserInRepo();
        this.token = this.usersUtil.getSuperUserToken();

        this.mapper = new ObjectMapper();

        // create users
        this.user1 = new User();
        this.user2 = new User();
        this.userRepository.save(this.user1);
        this.userRepository.save(this.user2);

        // get user id
        this.user1Id = this.user1.getId();
        this.user2Id = this.user2.getId();

        // create listings
        this.listing1 = new Listing(this.user1Id, "1", "1", 0.0F, "1", "1", "1", 5, "1", true);
        this.listing2 = new Listing(this.user1Id, "2", "2", 0.0F, "2", "2", "2", 5, "2", true);
        this.listingRepository.save(this.listing1);
        this.listingRepository.save(this.listing2);

    }

    @AfterEach
    public void resetTest() {
        // delete users
        this.userRepository.deleteById(this.user1.getId());
        this.userRepository.deleteById(this.user2.getId());
        // Reset the database to prevent tests from affecting each other
        this.cartItemRepository.deleteAll();
        this.listingRepository.deleteAll();
    }

    @Test
    void recommendation_test_one_not_seen() throws Exception {
        // create checked out cart items
        CartItem cartItem1 = new CartItem(this.user1Id, this.listing1, 1L);
        CartItem cartItem2 = new CartItem(this.user1Id, this.listing2, 1L);
        CartItem cartItem3 = new CartItem(this.user2Id, this.listing1, 1L);
        cartItem1.checkout();
        cartItem2.checkout();
        cartItem3.checkout();
        this.cartItemRepository.save(cartItem1);
        this.cartItemRepository.save(cartItem2);
        this.cartItemRepository.save(cartItem3);

        // get recommendations
        MvcResult result = this.mockMvc.perform(get("/recommendation/" + this.user2Id)
                        .header("Authorization", "Bearer " + this.token))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();
        Set recommendations = this.mapper.readValue(result.getResponse().getContentAsByteArray(), Set.class);
        Assert.isTrue(recommendations.size() == 1, "Recommendation list should have size 1");

        // delete users
        this.userRepository.deleteById(this.user1Id);
        this.userRepository.deleteById(this.user2Id);
    }

    @Test
    void recommendation_test_all_seen() throws Exception {
        // create checked out cart items
        CartItem cartItem1 = new CartItem(this.user1Id, this.listing1, 1L);
        CartItem cartItem2 = new CartItem(this.user1Id, this.listing2, 1L);
        CartItem cartItem3 = new CartItem(this.user2Id, this.listing1, 1L);
        CartItem cartItem4 = new CartItem(this.user2Id, this.listing2, 1L);
        cartItem1.checkout();
        cartItem2.checkout();
        cartItem3.checkout();
        cartItem4.checkout();
        this.cartItemRepository.save(cartItem1);
        this.cartItemRepository.save(cartItem2);
        this.cartItemRepository.save(cartItem3);
        this.cartItemRepository.save(cartItem4);

        // get recommendations
        MvcResult result = mockMvc.perform(get("/recommendation/" + this.user2Id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Set recommendations = mapper.readValue(result.getResponse().getContentAsByteArray(), Set.class);
        Assert.isTrue(recommendations.size() == 0, "Recommendation list should have size 0");

        // delete users
        userRepository.deleteById(this.user1Id);
        userRepository.deleteById(this.user2Id);
    }
}
