package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.AuthenticationSuperUserUtil;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.ListingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.SYSC4806_Group13.SYSC4806_Project.StaticTestUtilities.APPLICATION_JSON_UTF8;
import static com.SYSC4806_Group13.SYSC4806_Project.StaticTestUtilities.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ListingRestControllerTest {

    @Autowired
    private ListingRestController restController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    AuthenticationSuperUserUtil usersUtil;
    private String token;

    @BeforeEach
    public void beforeEachTest() {
        //Add the SuperUser and token
        usersUtil.setSuperUserInRepo();
        this.token = usersUtil.getSuperUserToken();
        // Reset the database to prevent tests from affecting each other
        listingRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        assertThat(restController).isNotNull();
    }

    @Test
    public void postGetPutDeleteListing_integration() throws Exception {
        MvcResult result;
        List list;
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<String, Object>();

        Integer sellerUserId = 123;
        Integer sellerUserId2 = 1234;
        String isbn = "123ABC";
        String title = "title";
        String title2 = "new title";
        String price = "1.5";
        String price2 = "10.5";
        String author = "author";
        String publisher = "publisher";
        String description = "description";
        Integer inventory = 5;
        Integer inventory2 = 500;
        String releaseDate = "05/08/22";

        map.put("sellerUserId", sellerUserId);
        map.put("isbn", isbn);
        map.put("title", title);
        map.put("price", price);
        map.put("author", author);
        map.put("publisher", publisher);
        map.put("description", description);
        map.put("inventory", inventory);
        map.put("releaseDate", releaseDate);

        // Create Listing 1
        mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer "+token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // Create Listing 2
        map.replace("sellerUserId", sellerUserId2);
        mockMvc.perform(post("/listings")
                        .header("Authorization", "Bearer "+token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // Get both listings
        result = mockMvc.perform(get("/listings")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assertions.assertEquals(2, list.size());
        Integer listing1_id = (Integer) ((Map<String, Object>) list.get(0)).get("listingId");

        // Get listings by sellerUserId
        result = mockMvc.perform(get("/listings?sellerUserId=123")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Assertions.assertEquals(1, list.size());

        // Update a listing
        map.put("listingId", listing1_id);
        map.replace("title", title2);
        map.replace("price", price2);
        map.replace("inventory", inventory2);

        mockMvc.perform(put("/listings")
                        .header("Authorization", "Bearer "+token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().is2xxSuccessful());

        // Get both listings
        result = mockMvc.perform(get("/listings")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        list = mapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
        Integer listingId = (Integer) ((Map<String, Object>) list.get(0)).get("listingId");

        // Check if listing was updated 6 was the autoincremeted result
        result = mockMvc.perform(get("/listings/" + listingId)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Object updatedListing = mapper.readValue(result.getResponse().getContentAsString(), Object.class);
        Assertions.assertTrue(updatedListing.toString().contains("title=" + title2));
        Assertions.assertTrue(updatedListing.toString().contains("price=" + price2));
        Assertions.assertTrue(updatedListing.toString().contains("inventory=" + inventory2));

        // Delete the listing
        mockMvc.perform(delete("/listings/" + listingId)
                .header("Authorization", "Bearer "+token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(map))
        ).andExpect(status().is2xxSuccessful());

        // Ensure the deleted listing is now inactive
        result = mockMvc.perform(get("/listings/" + listingId)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Object deletedListing = mapper.readValue(result.getResponse().getContentAsString(), Object.class);
        Assertions.assertTrue(deletedListing.toString().contains("active=false"));
    }

    @Test
    public void getListingFails() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // Listing does not exist
        mockMvc.perform(get("/listings/10")
                        .header("Authorization", "Bearer "+token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteListingFails() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // Listing does not exist
        mockMvc.perform(delete("/listings/10")
                        .header("Authorization", "Bearer "+token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void changeListingFails() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("listingId", 2);
        map.put("title", "new title");
        // Listing does not exist
        mockMvc.perform(put("/listings")
                        .header("Authorization", "Bearer "+token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(map))
                )
                .andExpect(status().isNotFound());
    }
}
