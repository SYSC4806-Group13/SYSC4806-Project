package com.SYSC4806_Group13.SYSC4806_Project.Model;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.ListingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

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
        Assert.isTrue(cartItemRepo.findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(userId, listing1.getListingId()).getId().equals(id), "Wrong entry retrieved");

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
        cartItemRepo.delete(cartItemRepo.findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(userId, listing1.getListingId()));
        Assert.isTrue(cartItemRepo.count() == 2, "Wrong number of entries");

        cartItemRepo.deleteAll();
    }

    @Test
    void cartItemCheckoutAndOrderHistoryTest() {
        cartItemRepo.deleteAll();
        Long userId = 123L;
        Long userId2 = 1234L;

        Listing listing1 = new Listing();
        listing1.setPrice(123.45f);
        listing1.setInventory(123);
        listingRepo.save(listing1);
        Listing listing2 = new Listing();
        listing2.setPrice(123.45f);
        listing2.setInventory(123);
        listingRepo.save(listing2);


        CartItem ci1 = new CartItem(userId, listing1, 1L);
        cartItemRepo.save(ci1);

        // Different listing
        CartItem ci2 = new CartItem(userId, listing2, 1L);
        cartItemRepo.save(ci2);

        //Different user ID from previous 2
        CartItem ci3 = new CartItem(userId2, listing1, 1L);
        cartItemRepo.save(ci3);

        Assert.isTrue(cartItemRepo.count() == 3, "Wrong number of entries");
        Assert.isTrue(cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(userId).size() == 0, "Should not have checked out items");
        List<CartItem> items = cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNull(userId);
        Assert.isTrue(items.size() == 2, "Should have 2 items in the cart");

        for (CartItem item : items) {
            Assert.isTrue(item.checkout(), "Checkout should return true");
            cartItemRepo.save(item);
        }

        Assert.isTrue(cartItemRepo.count() == 3, "Wrong number of entries"); // No removed items. They only change
        Assert.isTrue(cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNull(userId).size() == 0, "Should have no items in the cart");
        items = cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(userId);
        Assert.isTrue(items.size() == 2, "Should have 2 checked out items");

        for (CartItem item : items) {
            Assert.isTrue(!item.checkout(), "Checkout should return false for checked out items");
            Assert.isTrue(item.getTotalCartItemPriceAtPurchase() != null, "Should have a non null price at purchase");
            Assert.isTrue(item.getListing().getInventory() == 122, "Listing value did not get updated");
        }

        cartItemRepo.deleteAll();
    }
}
