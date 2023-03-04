package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CartItemRestController {

    private final CartItemRepository cartItemRepo;

    public CartItemRestController(CartItemRepository cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @GetMapping("/cartItems")
    public Object getUserCartItems(@RequestParam(value = "userID") long userID) {
        return cartItemRepo.findAllByUserID(userID);
    }

    @PostMapping("/cartItems")
    public Map<String, Object> postUserCartItem(@RequestBody Map<String, Long> payload) {
        Long userID = payload.get("userID");
        Long listingID = payload.get("listingID");
        Long quantity = payload.get("quantity");

        if (userID == null || listingID == null || quantity == null) {
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expecting request body to include userID, listingID, quantity");
        }

        if (quantity <= 0) {
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity cannot be non-positive");
        }

        if (cartItemRepo.findCartItemByUserIDAndListingID(userID, listingID) != null) {
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A cartItem for this user and listing already exists");
        }

        CartItem cartItem = new CartItem(userID, listingID, quantity);
        cartItemRepo.save(cartItem);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cartItemId", cartItem.getId());

        return map;
    }

    @PutMapping("/cartItems")
    public Map<String, Object> updateUserCartItem(@RequestBody Map<String, Long> payload) {
        Long userID = payload.get("userID");
        Long listingID = payload.get("listingID");
        Long quantity = payload.get("quantity");

        if (userID == null || listingID == null || quantity == null) {
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expecting request body to include userID, listingID, quantity");
        }

        if (quantity <= 0) {
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity cannot be non-positive");
        }

        CartItem cartItemToUpdate = cartItemRepo.findCartItemByUserIDAndListingID(userID, listingID);
        if (cartItemToUpdate == null) {
            // No matching listingID for this user
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cartItems with listingID=" + listingID + " for userID=" + userID);
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
        Long userID = payload.get("userID");
        Long listingID = payload.get("listingID");

        if (userID == null || listingID == null) {
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expecting request body to include userID, listingID");
        }

        CartItem cartItemToUpdate = cartItemRepo.findCartItemByUserIDAndListingID(userID, listingID);
        if (cartItemToUpdate == null) {
            // No matching listingID for this user
            // TODO: Update with HG's exception system once it's in
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cartItems with listingID=" + listingID + " for userID=" + userID);
        }

        cartItemRepo.delete(cartItemToUpdate);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "ok");
        return map;
    }
}
