package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Model.CartItemRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.ListingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
    private static final Long userId = 123L;
    @Autowired
    private CartItemRestController restController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ListingRepository listingRepository;

    @BeforeEach
    @AfterEach
    public void betweenEachTest() {
        // Reset the database to prevent tests from affecting each other
        cartItemRepository.deleteAll();
        listingRepository.deleteAll();
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

        long listingId1 = this.makeListingViaApiCall_returnListingId(123);
        long listingId2 = this.makeListingViaApiCall_returnListingId(123);


        // Setup all the params
        map.put("userId", userId);
        map.put("listingId", listingId1);
        map.put("quantity", 10L);

        // ADD LISTING ID 1
        mockMvc.perform(post("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ADD LISTING ID 1
        map.replace("listingId", listingId2);
        mockMvc.perform(post("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // GET BOTH CART ITEMS WITH DIFFERENT LISTING IDs
        result = mockMvc.perform(get("/cartItems?userId=123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that there are 2 cart items returned
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("userId=123"), "Incorrect userId");
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=10"), "Incorrect quantity");
        }

        // UPDATE BOTH CART ITEM QUANTITIES TO 2
        map.replace("quantity", 2L);
        mockMvc.perform(put("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());
        map.replace("listingId", listingId1);
        mockMvc.perform(put("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ENSURE QUANTITIES GOT UPDATED
        result = mockMvc.perform(get("/cartItems?userId=123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that their quantities are updated
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("userId=123"), "Incorrect userId");
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=2"), "Incorrect quantity");
        }

        // DELETE CART ITEM FOR LISTING ID 1
        mockMvc.perform(delete("/cartItems")
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure only one cart item remains
        result = mockMvc.perform(get("/cartItems?userId=123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 1, "Wrong size response");

        // DELETE CART ITEM FOR LISTING ID 2
        map.replace("listingId", listingId2);
        mockMvc.perform(delete("/cartItems")
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure empty response
        result = mockMvc.perform(get("/cartItems?userId=123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 0, "Wrong size response");
    }

    @Test
    public void deleteCartItemsFails() throws Exception {
        // Need all the params
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("userId", userId);
        map.put("listingId", this.makeListingViaApiCall_returnListingId(123));

        // Can't delete non-existing cartItems
        mockMvc.perform(delete("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addAlreadyExistingCartItems() throws Exception {
        // Need all the params
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("userId", userId);
        map.put("listingId", this.makeListingViaApiCall_returnListingId(123));
        map.put("quantity", 10L);


        // First post OK
        mockMvc.perform(post("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // post again and it fails
        mockMvc.perform(post("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void changeCartItemsFails() throws Exception {
        // Need all the params
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("userId", userId);
        map.put("listingId", this.makeListingViaApiCall_returnListingId(123));
        // NO quantity param
        mockMvc.perform(put("/cartItems")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());

        // Can't change cart items that don't exist, even with quantity
        map.replace("userId", 123456789L); //Invalid ID
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

        // Only accepted q-param is userId case-sensitive
        mockMvc.perform(get("/cartItems?UserID=1"))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/cartItems?badQueryParam=1"))
                .andExpect(status().is4xxClientError());

        // userId must be a long type
        mockMvc.perform(get("/cartItems?userId=string"))
                .andExpect(status().is4xxClientError());
    }


    private long makeListingViaApiCall_returnListingId(Integer sellerUserId) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        String isbn = "123ABC";
        String title = "title";
        String price = "1.5";
        String author = "author";
        String publisher = "publisher";
        String description = "description";
        Integer inventory = 5;
        String releaseDate = "05/08/22";
        String coverImage = "image url";

        map.put("sellerUserId", sellerUserId);
        map.put("isbn", isbn);
        map.put("title", title);
        map.put("price", price);
        map.put("author", author);
        map.put("publisher", publisher);
        map.put("description", description);
        map.put("inventory", inventory);
        map.put("releaseDate", releaseDate);
        map.put("coverImage", coverImage);

        // Create Listing 1
        MvcResult result = mockMvc.perform(post("/listings")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getResponse().getContentAsByteArray(), Listing.class).getListingId();

    }
}
