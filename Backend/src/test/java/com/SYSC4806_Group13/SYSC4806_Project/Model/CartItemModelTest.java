package com.SYSC4806_Group13.SYSC4806_Project.Model;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class CartItemModelTest {
    @Autowired
    CartItemRepository repo;

    @Test
    void cartItemPersistenceTest() {
        repo.deleteAll();

        CartItem ci1 = new CartItem();
        ci1.setUserID(123L);
        ci1.setListingID(456L);
        repo.save(ci1);

        CartItem ci2 = new CartItem();
        ci2.setUserID(123L);
        ci2.setListingID(456L);
        repo.save(ci2);

        Assert.isTrue(repo.count() == 2, "Extra data stored");
        for (CartItem currentCartItem : repo.findAll()) {
            Assert.isTrue(currentCartItem.getUserID().equals(123L), "Wrong User ID");
            Assert.isTrue(currentCartItem.getListingID().equals(456L), "Wrong Listing ID");
        }

        repo.deleteAll();
    }

    @Test
    void cartItemFindAllByUserIdTest() {
        repo.deleteAll();
        Long userId = 123L;
        CartItem ci1 = new CartItem();
        ci1.setUserID(userId);
        ci1.setListingID(4561L);
        repo.save(ci1);

        CartItem ci2 = new CartItem();
        ci2.setUserID(userId);
        ci2.setListingID(4562L);
        repo.save(ci2);

        CartItem ci3 = new CartItem();
        ci3.setUserID(3123L); //Different from previous 2
        ci3.setListingID(4562L);
        repo.save(ci3);

        Assert.isTrue(repo.count() == 3, "Extra data stored");
        for (CartItem currentCartItem : repo.findAllByUserID(userId)) {
            Assert.isTrue(currentCartItem.getUserID().equals(userId), "Wrong User ID");
        }

        repo.deleteAll();
    }

    @Test
    void cartItemFindByUserIdAndListingIdTest() {
        repo.deleteAll();

        Long userId = 123L;
        Long listingId = 456L;

        CartItem ci1 = new CartItem();
        ci1.setUserID(userId);
        ci1.setListingID(24561L);
        repo.save(ci1);

        CartItem ci2 = new CartItem();
        ci2.setUserID(userId);
        ci2.setListingID(listingId);
        repo.save(ci2);
        Long id = ci2.getId();

        CartItem ci3 = new CartItem();
        ci3.setUserID(3123L); //Different from previous 2
        ci3.setListingID(listingId);
        repo.save(ci3);

        Assert.isTrue(repo.findCartItemByUserIDAndListingID(userId, listingId).getId().equals(id), "Wrong entry retrieved");

        repo.deleteAll();
    }

    @Test
    void cartItemDeleteByUserIdAndListingIdTest() {
        repo.deleteAll();

        Long userId = 123L;
        Long listingId = 456L;

        CartItem ci1 = new CartItem();
        ci1.setUserID(userId);
        ci1.setListingID(24561L);
        repo.save(ci1);

        CartItem ci2 = new CartItem();
        ci2.setUserID(userId);
        ci2.setListingID(listingId);
        repo.save(ci2);

        CartItem ci3 = new CartItem();
        ci3.setUserID(3123L); //Different from previous 2
        ci3.setListingID(listingId);
        repo.save(ci3);

        Assert.isTrue(repo.count() == 3, "Wrong number of entries");
        repo.delete(repo.findCartItemByUserIDAndListingID(userId, listingId));
        Assert.isTrue(repo.count() == 2, "Wrong number of entries");


        repo.deleteAll();
    }
}
