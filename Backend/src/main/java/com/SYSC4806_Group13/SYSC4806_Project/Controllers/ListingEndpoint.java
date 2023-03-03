package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

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

@RestController
public class ListingEndpoint {

    private final ListingRepository repo;

    public ListingEndpoint(ListingRepository repo) {
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

    @GetMapping("/listings/{id}")
    public Listing getListing(@PathVariable String id) {
        Optional<Listing> listing = repo.findById(Long.parseLong(id));
        if (listing.isPresent()) {
            return listing.get();
        } else {
            throw new NotFoundException("Listing with id [" + id + "] cannot be found");
        }
    }

    @PostMapping("/listings")
    public Listing addListing(@RequestBody Map<String, Object> payload) {

        Integer sellerUserId = (Integer) payload.get("sellerUserId");
        if (sellerUserId == null) {
            throw new MissingAttributeException("Request body must contain 'sellerUserId'");
        }

        String isbn = (String) payload.get("isbn");
        if (isbn == null) {
            throw new MissingAttributeException("Request body must contain 'isbn'");
        }

        String title = (String) payload.get("title");
        if (title == null) {
            throw new MissingAttributeException("Request body must contain 'title'");
        }

        String price = (String) payload.get("price");
        Float floatPrice;

        if (price == null) {
            throw new MissingAttributeException("Request body must contain 'title'");
        }

        try {
            floatPrice = Float.valueOf(price);
        } catch (NumberFormatException e) {
            throw new MissingAttributeException("Attribute 'price' must be formatted in decimal as a string (ex. '1.5')");
        }

        String author = (String) payload.get("author");
        if (author == null) {
            throw new MissingAttributeException("Request body must contain 'author'");
        }

        String publisher = (String) payload.get("publisher");
        if (publisher == null) {
            throw new MissingAttributeException("Request body must contain 'publisher'");
        }

        String description = (String) payload.get("description");
        if (description == null) {
            throw new MissingAttributeException("Request body must contain 'description'");
        }

        Integer inventory = (Integer) payload.get("inventory");
        if (inventory == null) {
            throw new MissingAttributeException("Request body must contain 'inventory'");
        }

        String releaseDate = (String) payload.get("releaseDate");
        if (releaseDate == null) {
            throw new MissingAttributeException("Request body must contain 'releaseDate'");
        }

        String coverImage = (String) payload.get("coverImage");
        if (coverImage == null) {
            throw new MissingAttributeException("Request body must contain 'coverImage'");
        }


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
                true,
                coverImage
        );

        repo.save(listing);

        return listing;
    }

    @PutMapping("/listings")
    public Map<String, Object> putListing(@RequestBody Map<String, Object> payload) {
        Map<String, Object> map = new HashMap<>();

        Integer listingId = (Integer) payload.get("listingId");
        if (listingId == null) {
            throw new MissingAttributeException("Request body must contain 'listingId'");
        }

        Listing listing = repo.findByListingId(listingId.longValue());

        if (listing == null) {
            throw new NotFoundException("Listing with id [" + listingId + "] cannot be found");
        }

        String isbn = (String) payload.get("isbn");
        if (isbn != null) {
            listing.setISBN(isbn);
            map.put("isbn", isbn);
        }
        String title = (String) payload.get("title");
        if (title != null) {
            listing.setTitle(title);
            map.put("title", title);
        }

        String price = (String) payload.get("price");
        if (price != null) {
            try {
                Float floatPrice = Float.valueOf(price);
                listing.setPrice(floatPrice);
                map.put("price", price);
            } catch (NumberFormatException e) {
                throw new MissingAttributeException("Attribute 'price' must be formatted in decimal as a string (ex. '1.5')");
            }
            listing.setPrice(Float.valueOf(price));
        }

        String author = (String) payload.get("author");
        if (author != null) {
            listing.setAuthor(author);
            map.put("author", author);
        }
        String publisher = (String) payload.get("publisher");
        if (publisher != null) {
            listing.setPublisher(publisher);
            map.put("publisher", publisher);
        }
        String description = (String) payload.get("description");
        if (description != null) {
            listing.setDescription(description);
            map.put("description", description);
        }
        Integer inventory = (Integer) payload.get("inventory");
        if (inventory != null) {
            listing.setInventory(inventory);
            map.put("inventory", inventory);
        }
        String releaseDate = (String) payload.get("releaseDate");
        if (releaseDate != null) {
            listing.setReleaseDate(releaseDate);
            map.put("releaseDate", releaseDate);
        }

        String coverImage = (String) payload.get("coverImage");
        if (coverImage != null) {
            listing.setCoverImage(coverImage);
            map.put("coverImage", coverImage);
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
