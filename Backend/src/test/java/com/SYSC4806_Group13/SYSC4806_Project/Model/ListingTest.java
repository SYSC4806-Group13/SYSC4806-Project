package com.SYSC4806_Group13.SYSC4806_Project.Model;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.ListingRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Sysc4806ProjectApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Sysc4806ProjectApplication.class)
public class ListingTest {

    @Autowired
    ListingRepository repo;

    @Test
    void listingPersistenceTest() {
        repo.deleteAll();

        Listing l1 = new Listing();
        l1.setISBN("123");
        l1.setTitle("123");
        l1.setPrice(1.5f);
        l1.setAuthor("Tester");
        l1.setPublisher("Tester Publisher");
        l1.setDescription("This is a description");
        l1.setInventory(3);
        l1.setReleaseDate("date");
        l1.setActive(true);
        l1.setCoverImage("test url");
        repo.save(l1);

        Assertions.assertEquals(1, repo.count());
        for (Listing listing : repo.findAll()) {
            Assertions.assertEquals("123", listing.getISBN());
        }

        Assertions.assertNotNull(repo.findByListingId(l1.getListingId()));

        repo.deleteAll();
    }

    @Test
    void findListingBySellerUserIdTest() {
        repo.deleteAll();

        Listing l1 = new Listing();
        l1.setSellerUserId(123L);
        l1.setISBN("123");
        l1.setTitle("123");
        l1.setPrice(1.5f);
        l1.setAuthor("Tester");
        l1.setPublisher("Tester Publisher");
        l1.setDescription("This is a description");
        l1.setInventory(3);
        l1.setReleaseDate("date");
        l1.setActive(true);
        l1.setCoverImage("test url");
        repo.save(l1);

        Assertions.assertEquals(1, repo.count());
        Assertions.assertEquals(1, repo.findAllBySellerUserId(123L).size());

        repo.deleteAll();
    }

}
