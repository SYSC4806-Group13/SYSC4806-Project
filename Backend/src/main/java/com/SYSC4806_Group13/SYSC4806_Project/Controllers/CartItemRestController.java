package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.DuplicateException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.NotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.ListingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.SYSC4806_Group13.SYSC4806_Project.Utilities.ControllerValidationUtilities.getValidatedAttribute_NonNull;
import static com.SYSC4806_Group13.SYSC4806_Project.Utilities.ControllerValidationUtilities.getValidatedLongAttribute_positiveOnly;

@RestController
public class CartItemRestController {

    private final CartItemRepository cartItemRepo;
    private final ListingRepository listingRepo;

    public CartItemRestController(CartItemRepository cartItemRepo, ListingRepository listingRepo) {
        this.cartItemRepo = cartItemRepo;
        this.listingRepo = listingRepo;
    }

    @GetMapping("/cartItems")
    @ResponseBody()
    public List<CartItem> getUserCartItems(@RequestParam(value = "userId") long userId) {
        return cartItemRepo.findAllByUserId(userId);
    }

    @PostMapping("/cartItems")
    public Map<String, Object> postUserCartItem(@RequestBody Map<String, Long> payload) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", payload.get("userId"));
        Long listingId = (Long) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));
        Long quantity = getValidatedLongAttribute_positiveOnly("quantity", payload.get("quantity"), true);

        if (cartItemRepo.findCartItemByUserIdAndListing_ListingId(userId, listingId) != null) {
            throw new DuplicateException("A cartItem for this user and listing already exists");
        }

        Listing listing = listingRepo.findByListingId(listingId);
        if (listing == null) {
            throw new NotFoundException("ListingId=" + listingId + " does not exist");
        }

        CartItem cartItem = new CartItem(userId, listing, quantity);
        cartItemRepo.save(cartItem);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cartItemId", cartItem.getId());

        return map;
    }

    @PutMapping("/cartItems")
    public Map<String, Object> updateUserCartItem(@RequestBody Map<String, Long> payload) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", payload.get("userId"));
        Long listingId = (Long) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));
        Long quantity = getValidatedLongAttribute_positiveOnly("quantity", payload.get("quantity"), true);

        CartItem cartItemToUpdate = cartItemRepo.findCartItemByUserIdAndListing_ListingId(userId, listingId);
        if (cartItemToUpdate == null) {
            // No matching  for this user
            throw new NotFoundException("No cartItems with listingId=" + listingId + " for userId=" + userId);
        }

        // only the quantity can be changed
        cartItemToUpdate.setQuantity(quantity);
        cartItemRepo.save(cartItemToUpdate);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cartItemId", cartItemToUpdate.getId());

        return map;
    }

    @DeleteMapping("/cartItems")
    public Map<String, Object> deleteUserCartItem(@RequestBody Map<String, Long> payload) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", payload.get("userId"));
        Long listingId = (Long) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));

        CartItem cartItemToUpdate = cartItemRepo.findCartItemByUserIdAndListing_ListingId(userId, listingId);
        if (cartItemToUpdate == null) {
            // No matching  for this user
            throw new NotFoundException("No cartItems with listingId=" + listingId + " for userId=" + userId);
        }

        cartItemRepo.delete(cartItemToUpdate);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "ok");
        return map;
    }
}
