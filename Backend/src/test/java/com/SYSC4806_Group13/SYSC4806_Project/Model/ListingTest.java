package com.SYSC4806_Group13.SYSC4806_Project.Model;

import com.SYSC4806_Group13.SYSC4806_Project.Sysc4806ProjectApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
//        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
//        Timestamp current = Timestamp.valueOf(ldt);
//        l1.setReleaseDate(current);
        l1.setReleaseDate("date");
        l1.setActive(true);
        l1.setCoverImage("test url");
        repo.save(l1);

        Assertions.assertEquals(1, repo.count());
        for (Listing listing : repo.findAll()) {
            Assertions.assertEquals("123", listing.getISBN());
        }

        repo.deleteAll();

    }

}
