package com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
//    CartItem findById(long id);

    /**
     * Retrieves all current cart items and order history
     */
    List<CartItem> findAllByUserId(long userId);

    /**
     * Retrieves the cart items for the current user that have not been checked out yet
     */
    List<CartItem> findAllByUserIdAndPurchaseDateTimeIsNull(long userId);

    /**
     * Retrieves order history for the current user
     */
    List<CartItem> findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(long userId);

    /**
     * Retrieves all active cart items for the given listing ID
     */
    List<CartItem> findAllByListing_ListingIdAndPurchaseDateTimeIsNull(long listingId);

    /**
     * Retrieves cart item that is not checked out
     */
    CartItem findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(long userId, long listingId);
}
