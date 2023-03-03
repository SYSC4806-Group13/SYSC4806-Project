package com.SYSC4806_Group13.SYSC4806_Project.Model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    CartItem findById(long id);

    List<CartItem> findAllByUserID(long userID);

    CartItem findCartItemByUserIDAndListingID(long userID, long listingID);
}
