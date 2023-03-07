package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.BadAttributeException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.MissingAttributeException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.NotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.ListingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.SYSC4806_Group13.SYSC4806_Project.Controllers.ControllerValidationUtilities.*;

@RestController
public class ListingRestController {

    private final ListingRepository repo;

    public ListingRestController(ListingRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/listings")
    public List<Listing> getListings(@RequestParam(value = "sellerUserId", required = false) Long sellerUserId) {
        if (sellerUserId == null) {
            // Return all listings
            Iterable<Listing> iterable = repo.findAll();
            return Streamable.of(iterable).toList();
        } else {
            // Return listings matching given sellerUserId
            return repo.findAllBySellerUserId(sellerUserId);
        }
    }

    @GetMapping("/listings/{listingId}")
    public Listing getListing(@PathVariable String listingId) {
        Optional<Listing> listing = repo.findById(Long.parseLong(listingId));
        if (listing.isPresent()) {
            return listing.get();
        } else {
            throw new NotFoundException("Listing with id [" + listingId + "] cannot be found");
        }
    }

    @PostMapping("/listings")
    public Listing addListing(@RequestBody Map<String, Object> payload) {
        Integer sellerUserId = (Integer) getValidatedAttribute_NonNull("sellerUserId", payload.get("sellerUserId"));
        String title = getValidatedStringAttribute_NonNullNonBlank("title", (String) payload.get("title"));
        String author = getValidatedStringAttribute_NonNullNonBlank("author", (String) payload.get("author"));
        String publisher = getValidatedStringAttribute_NonNullNonBlank("publisher", (String) payload.get("publisher"));
        String description = getValidatedStringAttribute_NonNullNonBlank("description", (String) payload.get("description"));
        String releaseDate = getValidatedStringAttribute_NonNullNonBlank("releaseDate", (String) payload.get("releaseDate"));
        String isbn = getValidatedStringAttribute_NonNullNonBlank("isbn", (String) payload.get("isbn"));
        Integer inventory = getValidatedIntegerAttribute_positiveOnly("inventory", (Integer) payload.get("inventory"), true);

        // TODO: Finalize approach to handle image storage. Consider FTP protocols with remote image hosting servers and saving links here
        String coverImage = getValidatedStringAttribute_NonNullNonBlank("coverImage", (String) payload.get("coverImage"));


        Float floatPrice;
        try {
            String price = (String) payload.get("price");
            if (price == null) {
                throw new MissingAttributeException("Request body must contain 'price'");
            }
            floatPrice = Float.valueOf(price);
            if (floatPrice < 0) {
                throw new BadAttributeException("Attribute 'price' cannot be negative");
            }
        } catch (NumberFormatException | ClassCastException e) {
            throw new MissingAttributeException("Attribute 'price' must be formatted in decimal as a string (ex. '1.5')");
        }


        Boolean isActive = true;

        Listing listing = new Listing(
                sellerUserId.longValue(),
                isbn,
                title,
                floatPrice,
                author,
                publisher,
                description,
                inventory,
                releaseDate,
                isActive,
                coverImage
        );

        repo.save(listing);

        return listing;
    }

    @PutMapping("/listings")
    public Map<String, Object> putListing(@RequestBody Map<String, Object> payload) {
        Map<String, Object> map = new HashMap<>();
        Integer listingId = (Integer) getValidatedAttribute_NonNull("listingId", payload.get("listingId"));

        Listing listing = repo.findByListingId(listingId.longValue());

        if (listing == null) {
            throw new NotFoundException("Listing with id [" + listingId + "] cannot be found");
        }


        if (setValidatedStringAttributeToMap("isbn", (String) payload.get("isbn"), map)) {
            listing.setISBN((String) payload.get("isbn"));
        }
        if (setValidatedStringAttributeToMap("title", (String) payload.get("title"), map)) {
            listing.setTitle((String) payload.get("title"));
        }
        if (setValidatedStringAttributeToMap("author", (String) payload.get("author"), map)) {
            listing.setAuthor((String) payload.get("author"));
        }
        if (setValidatedStringAttributeToMap("publisher", (String) payload.get("publisher"), map)) {
            listing.setPublisher((String) payload.get("publisher"));
        }
        if (setValidatedStringAttributeToMap("description", (String) payload.get("description"), map)) {
            listing.setDescription((String) payload.get("description"));
        }
        if (setValidatedStringAttributeToMap("releaseDate", (String) payload.get("releaseDate"), map)) {
            listing.setReleaseDate((String) payload.get("releaseDate"));
        }
        if (setValidatedIntegerAttributeToMap("inventory", (Integer) payload.get("inventory"), map, false)) {
            listing.setInventory((Integer) payload.get("inventory"));
        }
        if (setValidatedStringAttributeToMap("coverImage", (String) payload.get("coverImage"), map)) {
            // TODO: Finalize approach to handle image storage. Consider FTP protocols with remote image hosting servers and saving links here
            listing.setCoverImage((String) payload.get("coverImage"));
        }

        try {
            String price = (String) payload.get("price");
            if (price != null) {
                Float floatPrice = Float.valueOf(price);
                if (floatPrice < 0) {
                    throw new BadAttributeException("Attribute 'price' cannot be negative");
                }
                listing.setPrice(floatPrice);
                map.put("price", price);
                listing.setPrice(floatPrice);
            }
        } catch (NumberFormatException | ClassCastException e) {
            throw new MissingAttributeException("Attribute 'price' must be formatted in decimal as a string (ex. '1.5')");
        }

        repo.save(listing);
        return map;
    }

    @DeleteMapping("/listings/{id}")
    public Listing deleteListing(@PathVariable String id) {
        Optional<Listing> listing = repo.findById(Long.parseLong(id));
        if (listing.isPresent()) {
            listing.get().setActive(false);
            repo.save(listing.get());
            return listing.get();
        } else {
            throw new NotFoundException("Listing with id [" + id + "] cannot be found");
        }
    }

}
