package com.SYSC4806_Group13.SYSC4806_Project.Model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class CartItemModelTest {
    @Autowired
    CartItemRepository cartItemRepo;

    @Autowired
    ListingRepository listingRepo;

    @Test
    void cartItemPersistenceTest() {
        cartItemRepo.deleteAll();

        Listing listing = new Listing();
        listingRepo.save(listing);

        CartItem ci1 = new CartItem(123L, listing, 1L);
        cartItemRepo.save(ci1);

        CartItem ci2 = new CartItem(123L, listing, 1L);
        cartItemRepo.save(ci2);

        Assert.isTrue(cartItemRepo.count() == 2, "Extra data stored");
        for (CartItem currentCartItem : cartItemRepo.findAll()) {
            Assert.isTrue(currentCartItem.getUserId().equals(123L), "Wrong User ID");
            Assert.isTrue(currentCartItem.getListing().getListingId().equals(listing.getListingId()), "Wrong Listing ID");
        }

        cartItemRepo.deleteAll();
    }

    @Test
    void cartItemFindAllByUserIdTest() {
        cartItemRepo.deleteAll();
        Long userId = 123L;

        CartItem ci1 = new CartItem(userId, null, 1L);
        cartItemRepo.save(ci1);

        CartItem ci2 = new CartItem(userId, null, 1L);
        cartItemRepo.save(ci2);

        // Different from previous 2
        CartItem ci3 = new CartItem(3123L, null, 1L);
        cartItemRepo.save(ci3);

        Assert.isTrue(cartItemRepo.count() == 3, "Extra data stored");
        for (CartItem currentCartItem : cartItemRepo.findAllByUserId(userId)) {
            Assert.isTrue(currentCartItem.getUserId().equals(userId), "Wrong User ID");
        }

        cartItemRepo.deleteAll();
    }

    @Test
    void cartItemFindByUserIdAndListingIdTest() {
        cartItemRepo.deleteAll();

        Long userId = 123L;

        Listing listing1 = new Listing();
        listingRepo.save(listing1);
        Listing listing2 = new Listing();
        listingRepo.save(listing2);

        CartItem ci1 = new CartItem(userId, listing1, 1L);
        cartItemRepo.save(ci1);
        Long id = ci1.getId();

        // Different listing from previous
        CartItem ci2 = new CartItem(userId, listing2, 1L);
        cartItemRepo.save(ci2);

        // Different from previous 2
        CartItem ci3 = new CartItem(1234L, listing1, 1L);
        cartItemRepo.save(ci3);

        // Assert we get ci1 with userId=123L and listing1.id
        Assert.isTrue(cartItemRepo.findCartItemByUserIdAndListing_ListingId(userId, listing1.getListingId()).getId().equals(id), "Wrong entry retrieved");

        cartItemRepo.deleteAll();
    }

    @Test
    void cartItemDeleteByUserIdAndListingIdTest() {
        cartItemRepo.deleteAll();

        Long userId = 123L;

        Listing listing1 = new Listing();
        listingRepo.save(listing1);
        Listing listing2 = new Listing();
        listingRepo.save(listing2);


        CartItem ci1 = new CartItem(userId, listing1, 1L);
        cartItemRepo.save(ci1);

        // Different listing
        CartItem ci2 = new CartItem(userId, listing2, 1L);
        cartItemRepo.save(ci2);

        //Different user ID from previous 2
        CartItem ci3 = new CartItem(1234L, listing1, 1L);
        cartItemRepo.save(ci3);

        Assert.isTrue(cartItemRepo.count() == 3, "Wrong number of entries");
        cartItemRepo.delete(cartItemRepo.findCartItemByUserIdAndListing_ListingId(userId, listing1.getListingId()));
        Assert.isTrue(cartItemRepo.count() == 2, "Wrong number of entries");

        cartItemRepo.deleteAll();
    }
}
