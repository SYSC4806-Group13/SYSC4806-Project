package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import com.SYSC4806_Group13.SYSC4806_Project.AuthenticationSuperUserUtil;
import com.SYSC4806_Group13.SYSC4806_Project.Security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;
import java.util.HashMap;
import java.util.List;

import static com.SYSC4806_Group13.SYSC4806_Project.StaticTestUtilities.APPLICATION_JSON_UTF8;
import static com.SYSC4806_Group13.SYSC4806_Project.StaticTestUtilities.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartItemRestControllerTest {

    //Constants
    private static final Long userID = 123L;
    private static final Long listingID1 = 234L;
    private static final Long listingID2 = 456L;
    @Autowired
    private CartItemRestController restController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    AuthenticationSuperUserUtil usersUtil;
    private String token;

    @BeforeEach
    public void beforeEachTest() {
        //Add the SuperUser and token
        usersUtil.setSuperUserInRepo();
        this.token = usersUtil.getSuperUserToken();
        // Reset the database to prevent tests from affecting each other
        cartItemRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        assertThat(restController).isNotNull();
    }

    @Test
    public void addGetChangeGetDeleteCartItems_integration() throws Exception {
        // Needed variables
        MvcResult result;
        List list;
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Long> map = new HashMap<String, Long>();

        // Setup all the params
        map.put("userID", userID);
        map.put("listingID", listingID1);
        map.put("quantity", 10L);

        // ADD LISTING ID 1
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ADD LISTING ID 1
        map.replace("listingID", listingID2);
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // GET BOTH CART ITEMS WITH DIFFERENT LISTING IDs
        result = mockMvc.perform(get("/cartItems?userID=123")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that there are 2 cart items returned
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("userID=123"), "Incorrect userID");
            Assert.isTrue(ci.toString().contains("listingID="), "Incorrect listingID");
            Assert.isTrue(ci.toString().contains("quantity=10"), "Incorrect quantity");
        }

        // UPDATE BOTH CART ITEM QUANTITIES TO 2
        map.replace("quantity", 2L);
        mockMvc.perform(put("/cartItems")
                        .header("Authorization", "Bearer " + token).header("authentication", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());
        map.replace("listingID", listingID1);
        mockMvc.perform(put("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ENSURE QUANTITIES GOT UPDATED
        result = mockMvc.perform(get("/cartItems?userID=123")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that their quantities are updated
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("userID=123"), "Incorrect userID");
            Assert.isTrue(ci.toString().contains("listingID="), "Incorrect listingID");
            Assert.isTrue(ci.toString().contains("quantity=2"), "Incorrect quantity");
        }

        // DELETE CART ITEM FOR LISTING ID 1
        mockMvc.perform(delete("/cartItems")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure only one cart item remains
        result = mockMvc.perform(get("/cartItems?userID=123")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 1, "Wrong size response");

        // DELETE CART ITEM FOR LISTING ID 2
        map.replace("listingID", listingID2);
        mockMvc.perform(delete("/cartItems")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure empty response
        result = mockMvc.perform(get("/cartItems?userID=123")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 0, "Wrong size response");
    }

    @Test
    public void deleteCartItemsFails() throws Exception {
        // Need all the params
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("userID", userID);
        map.put("listingID", listingID1);

        // Can't delete non-existing cartItems
        mockMvc.perform(delete("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addAlreadyExistingCartItems() throws Exception {
        // Need all the params
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("userID", userID);
        map.put("listingID", listingID1);
        map.put("quantity", 10L);

        // First post OK
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // post again and it fails
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void changeCartItemsFails() throws Exception {
        // Need all the params
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("userID", userID);
        map.put("listingID", listingID1);
        // NO quantity param
        mockMvc.perform(put("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());

        // Can't change cart items that don't exist
        map = new HashMap<String, Long>();
        map.put("userID", userID);
        map.put("listingID", listingID1);
        map.put("quantity", 10L);
        mockMvc.perform(put("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getCartItemsFails() throws Exception {
        // Needs query parameter for ID
        mockMvc.perform(get("/cartItems"))
                .andExpect(status().is4xxClientError());

        // Only accepted q-param is userID case-sensitive
        mockMvc.perform(get("/cartItems?userId=1"))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/cartItems?badQueryParam=1"))
                .andExpect(status().is4xxClientError());

        // userID must be a long type
        mockMvc.perform(get("/cartItems?userID=string"))
                .andExpect(status().is4xxClientError());
    }
}
