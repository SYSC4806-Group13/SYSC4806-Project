package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.BadRequestException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.DuplicateException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.NotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.CartItem;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.CartItemRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.ListingRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Security.CurrentUser;
import com.SYSC4806_Group13.SYSC4806_Project.Security.UserPrincipal;
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

    @GetMapping("/orderHistory")
    @ResponseBody()
    public List<CartItem> getOrderHistoryCartItems(@CurrentUser UserPrincipal userPrincipal) {
        return cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(userPrincipal.getId());
    }

    @PostMapping("/checkout")
    @ResponseBody()
    public float checkoutCartItems(@CurrentUser UserPrincipal userPrincipal) {
        List<CartItem> cartItems = getUserCurrentCartItems(userPrincipal);
        for (CartItem ci : cartItems) {
            if (!ci.checkout()) {
                // Error due to insufficient listing inventory or somehow already being checked out
                throw new BadRequestException("Invalid cart state");
            }
        }

        //Once all checkouts are validated, save all of them
        float totalCartCost = 0;
        for (CartItem ci : cartItems) {
            totalCartCost += ci.getTotalCartItemPriceAtPurchase();
            cartItemRepo.save(ci);
            updateListingInventory(ci.getListing().getListingId(), cartItemRepo);
        }
        return totalCartCost;
    }

    @GetMapping("/cartItems")
    @ResponseBody()
    public List<CartItem> getUserCurrentCartItems(@CurrentUser UserPrincipal userPrincipal) {
        return cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNull(userPrincipal.getId());
    }

    //Given user and listing, return quantity and inventory
    @GetMapping("/cartItem")
    @ResponseBody()
    public Map<String, Integer> getCartItem(@CurrentUser UserPrincipal userPrincipal, @RequestParam(value = "listingId") long listingId) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", userPrincipal.getId());
        Long validatedListingId = (Long) getValidatedAttribute_NonNull("listingId", listingId);

        CartItem ci = cartItemRepo.findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(userId, validatedListingId);
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (ci == null) {
            map.put("quantity", 0);
            map.put("inventory", listingRepo.findByListingId(validatedListingId).getInventory());
        } else {
            map.put("quantity", ci.getQuantity().intValue());
            map.put("inventory", ci.getListing().getInventory());
        }
        return map;
    }

    @PostMapping("/cartItems")
    @ResponseBody()
    public CartItem postUserCartItem(@RequestBody Map<String, Long> payload, @CurrentUser UserPrincipal userPrincipal) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", userPrincipal.getId());
        Long listingId = (Long) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));
        Long quantity = getValidatedLongAttribute_positiveOnly("quantity", payload.get("quantity"), true);

        if (cartItemRepo.findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(userId, listingId) != null) {
            throw new DuplicateException("A cartItem for this user and listing already exists");
        }

        Listing listing = listingRepo.findByListingId(listingId);
        if (listing == null) {
            throw new NotFoundException("ListingId=" + listingId + " does not exist");
        }
        if (listing.getInventory() < quantity) {
            throw new BadRequestException("Cart item quantity cannot be greater than current listing inventory of " + listing.getInventory());
        }

        CartItem cartItem = new CartItem(userId, listing, quantity);
        cartItemRepo.save(cartItem);

        return cartItem;
    }

    @PutMapping("/cartItems")
    @ResponseBody()
    public CartItem updateUserCartItem(@RequestBody Map<String, Long> payload, @CurrentUser UserPrincipal userPrincipal) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", userPrincipal.getId());
        Long listingId = (Long) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));
        Long quantity = getValidatedLongAttribute_positiveOnly("quantity", payload.get("quantity"), true);

        CartItem cartItemToUpdate = cartItemRepo.findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(userId, listingId);
        if (cartItemToUpdate == null) {
            // No matching  for this user
            throw new NotFoundException("No cartItems with listingId=" + listingId + " for userId=" + userId);
        }
        Listing listing = listingRepo.findByListingId(listingId);
        if (listing.getInventory() < quantity) {
            throw new BadRequestException("Cart item quantity cannot be greater than current listing inventory of " + listing.getInventory());
        }

        // only the quantity can be changed
        cartItemToUpdate.setQuantity(quantity);
        cartItemRepo.save(cartItemToUpdate);

        return cartItemToUpdate;
    }

    @DeleteMapping("/cartItems")
    @ResponseBody()
    public Map<String, Integer> deleteUserCartItem(@RequestBody Map<String, Long> payload, @CurrentUser UserPrincipal userPrincipal) {
        Long userId = (Long) getValidatedAttribute_NonNull("userId", userPrincipal.getId());
        Long listingId = (Long) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));

        CartItem cartItemToUpdate = cartItemRepo.findCartItemByUserIdAndListing_ListingIdAndPurchaseDateTimeIsNull(userId, listingId);
        if (cartItemToUpdate == null) {
            // No matching  for this user
            throw new NotFoundException("No cartItems with listingId=" + listingId + " for userId=" + userId);
        }

        int inventory = cartItemToUpdate.getListing().getInventory();
        cartItemRepo.delete(cartItemToUpdate);

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("inventory", inventory);
        return map;
    }

    public static void updateListingInventory(Long listingId, CartItemRepository cartItemRepo) {
        for (CartItem ci : cartItemRepo.findAllByListing_ListingIdAndPurchaseDateTimeIsNull(listingId)) {
            int newInventory = ci.getListing().getInventory();
            if (newInventory <= 0) {
                // Now out of stock. Delete associated active cartItems
                cartItemRepo.delete(ci);
            } else {
                // Non-zero updated inventory
                // Ensure cart item quantity is less than the updated inventory
                ci.setQuantity(Math.min(ci.getQuantity(), newInventory));
                cartItemRepo.save(ci);
            }
        }

    }
}
