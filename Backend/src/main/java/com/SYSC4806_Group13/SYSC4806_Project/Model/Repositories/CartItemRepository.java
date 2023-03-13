package com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    CartItem findById(long id);

    List<CartItem> findAllByUserId(long userId);

    CartItem findCartItemByUserIdAndListing_ListingId(long userId, long listingId);
}
