package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.AuthenticationSuperUserUtil;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.ListingRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Security.TokenProvider;
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
    private static final Long userId = 1L;
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

    @Autowired
    private ListingRepository listingRepository;

    @BeforeEach
    @AfterEach
    public void betweenEachTest() {
        //Add the SuperUser and token
        usersUtil.setSuperUserInRepo();
        this.token = usersUtil.getSuperUserToken();
        // Reset the database to prevent tests from affecting each other
        cartItemRepository.deleteAll();
        listingRepository.deleteAll();
    }


    @Test
    public void contextLoads() {
        assertThat(restController).isNotNull();
    }

    @Test
    void inventoryChangeTest_integration() throws Exception {
        // Needed variables
        MvcResult result;
        List list;
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Long> map = new HashMap<String, Long>();

        long listingId1 = this.makeListingViaApiCall_returnListingId(1, 10);

        // Setup all the params
        map.put("listingId", listingId1);
        map.put("quantity", 7L);

        // ADD LISTING ID 1 FOR USER 1
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // GET CART ITEMS FOR USER
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // Ensure that the cart item is there and it has a quantity of 7
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 1, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=7"), "Incorrect quantity");
        }

        //Change inventory to be less than the cart quantity
        editListingViaApiCall(1, listingId1);
        // GET CART ITEMS FOR USER AGAIN
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // Quantity should have been reduced to 1 in response to inventory change
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 1, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=1"), "Incorrect quantity");
        }

        //Change inventory to be out of stock at zero
        editListingViaApiCall(0, listingId1);
        // GET CART ITEMS FOR USER AGAIN
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // Going out of stock should auto remove from cart
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 0, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=1"), "Incorrect quantity");
        }

        //Change inventory to be in stock again
        editListingViaApiCall(1234567, listingId1);
        // ADD TO CART
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // GET CART ITEMS FOR USER
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // Ensure that the cart item is there and it has a quantity of 7
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 1, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=7"), "Incorrect quantity");
        }


        // Delete/deactivate the listing
        deleteListingViaApiCall(listingId1);
        // GET CART ITEMS FOR USER AGAIN
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // Deleted listings are auto removed from carts
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 0, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=1"), "Incorrect quantity");
        }
    }

    @Test
    void checkoutTest_integration() throws Exception {
        // Needed variables
        MvcResult result;
        List list;
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Long> map = new HashMap<String, Long>();

        long listingId1 = this.makeListingViaApiCall_returnListingId(1, 123);
        long listingId2 = this.makeListingViaApiCall_returnListingId(1, 10);
        // Setup all the params
        map.put("userId", userId);
        map.put("listingId", listingId1);
        map.put("quantity", 10L);

        // ADD LISTING ID 1
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ADD LISTING ID 2
        map.replace("listingId", listingId2);
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // GET BOTH CART ITEMS WITH DIFFERENT LISTING IDs
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that there are 2 cart items returned
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=10"), "Incorrect quantity");
        }

        // GET EMPTY ORDER HISTORY
        result = mockMvc.perform(get("/orderHistory")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //Ensure it's empty
        Assert.isTrue(mapper.readValue(result.getResponse().getContentAsByteArray(), List.class).size() == 0, "Wrong size response");

        // CHECKOUT THE CART
        result = mockMvc.perform(post("/checkout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // The cart cost is returned
        Assert.isTrue(mapper.readValue(result.getResponse().getContentAsByteArray(), float.class) >= 0, "Expecting non zero total cart cost");

        // GET CURRENT CART AND ENSURE EMPTY
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Assert.isTrue(mapper.readValue(result.getResponse().getContentAsByteArray(), List.class).size() == 0, "Wrong size response");

        // GET ORDER HISTORY AND ENSURE NON-EMPTY
        result = mockMvc.perform(get("/orderHistory")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
    }

    @Test
    public void addGetChangeGetDeleteCartItems_integration() throws Exception {
        // Needed variables
        MvcResult result;
        List list;
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Long> map = new HashMap<String, Long>();

        long listingId1 = this.makeListingViaApiCall_returnListingId(1, 123);
        long listingId2 = this.makeListingViaApiCall_returnListingId(1, 123);


        // Setup all the params
        map.put("userId", userId);
        map.put("listingId", listingId1);
        map.put("quantity", 10L);

        // ADD LISTING ID 1
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ADD LISTING ID 2
        map.replace("listingId", listingId2);
        mockMvc.perform(post("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // GET BOTH CART ITEMS WITH DIFFERENT LISTING IDs
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that there are 2 cart items returned
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
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
        map.replace("listingId", listingId1);
        mockMvc.perform(put("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // ENSURE QUANTITIES GOT UPDATED
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Ensure that their quantities are updated
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 2, "Wrong size response");
        for (Object ci : list) {
            Assert.isTrue(ci.toString().contains("listingId="), "Incorrect listingId");
            Assert.isTrue(ci.toString().contains("quantity=2"), "Incorrect quantity");
        }

        // DELETE CART ITEM FOR LISTING ID 1
        mockMvc.perform(delete("/cartItems")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure only one cart item remains
        result = mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assert.isTrue(list.size() == 1, "Wrong size response");

        // DELETE CART ITEM FOR LISTING ID 2
        map.replace("listingId", listingId2);
        mockMvc.perform(delete("/cartItems")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure empty response
        result = mockMvc.perform(get("/cartItems")
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
        map.put("userId", userId);
        map.put("listingId", this.makeListingViaApiCall_returnListingId(123, 123));

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
        map.put("userId", userId);
        map.put("listingId", this.makeListingViaApiCall_returnListingId(123, 123));
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
        map.put("userId", userId);
        map.put("listingId", this.makeListingViaApiCall_returnListingId(123, 123));
        // NO quantity param
        mockMvc.perform(put("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());

        // Quantity param greater than inventory
        map.put("quantity", 111111L);
        mockMvc.perform(put("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());

        // Can't change cart items that don't exist, even with quantity
        map.replace("userId", 123456789L); //Invalid ID
        map.replace("quantity", 10L);
        mockMvc.perform(put("/cartItems")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getCartItemsFails() throws Exception {
        // Needs a proper user
        mockMvc.perform(get("/cartItems")
                        .header("Authorization", "Bearer " + "token"))
                .andExpect(status().is4xxClientError());

    }


    private long makeListingViaApiCall_returnListingId(Integer sellerUserId, Integer listingInventory) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("sellerUserId", sellerUserId);
        map.put("isbn", "123ABC");
        map.put("title", "title");
        map.put("price", "1.5");
        map.put("author", "author");
        map.put("publisher", "publisher");
        map.put("description", "description");
        map.put("inventory", listingInventory);
        map.put("releaseDate", "05/08/22");

        // Create Listing 1
        MvcResult result = mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getResponse().getContentAsByteArray(), Listing.class).getListingId();
    }

    private void editListingViaApiCall(long listingInventory, long listingId) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("listingId", listingId);
        map.put("inventory", listingInventory);

        // Update Listing
        mockMvc.perform(put("/listings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());
    }

    private void deleteListingViaApiCall(long listingId) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("listingId", listingId);

        // Update Listing
        mockMvc.perform(delete("/listings/" + listingId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());
    }
}
