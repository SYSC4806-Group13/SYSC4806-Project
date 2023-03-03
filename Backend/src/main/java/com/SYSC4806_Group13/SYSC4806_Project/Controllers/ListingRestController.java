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

        Integer sellerUserId = (Integer) payload.get("sellerUserId");
        if (sellerUserId == null) {
            throw new MissingAttributeException("Request body must contain 'sellerUserId'");
        }

        String isbn = (String) payload.get("isbn");
        if (isbn == null) {
            throw new MissingAttributeException("Request body must contain 'isbn'");
        }
        if (isbn.isBlank()) {
            throw new BadAttributeException("Attribute 'isbn' cannot be empty");
        }

        String title = (String) payload.get("title");
        if (title == null) {
            throw new MissingAttributeException("Request body must contain 'title'");
        }
        if (title.isBlank()) {
            throw new BadAttributeException("Attribute 'title' cannot be empty");
        }

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

        String author = (String) payload.get("author");
        if (author == null) {
            throw new MissingAttributeException("Request body must contain 'author'");
        }
        if (author.isBlank()) {
            throw new BadAttributeException("Attribute 'author' cannot be empty");
        }

        String publisher = (String) payload.get("publisher");
        if (publisher == null) {
            throw new MissingAttributeException("Request body must contain 'publisher'");
        }
        if (publisher.isBlank()) {
            throw new BadAttributeException("Attribute 'publisher' cannot be empty");
        }

        String description = (String) payload.get("description");
        if (description == null) {
            throw new MissingAttributeException("Request body must contain 'description'");
        }
        if (description.isBlank()) {
            throw new BadAttributeException("Attribute 'description' cannot be empty");
        }

        Integer inventory = (Integer) payload.get("inventory");
        if (inventory == null) {
            throw new MissingAttributeException("Request body must contain 'inventory'");
        }
        if (inventory < 0) {
            throw new BadAttributeException("Attribute 'inventory' cannot be negative");
        }

        String releaseDate = (String) payload.get("releaseDate");
        if (releaseDate == null) {
            throw new MissingAttributeException("Request body must contain 'releaseDate'");
        }
        if (releaseDate.isBlank()) {
            throw new BadAttributeException("Attribute 'releaseDate' cannot be empty");
        }

        String coverImage = (String) payload.get("coverImage");
        if (coverImage == null) {
            throw new MissingAttributeException("Request body must contain 'coverImage'");
        }
        if (coverImage.isBlank()) {
            throw new BadAttributeException("Attribute 'coverImage' cannot be empty");
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
            if (isbn.isBlank()) {
                throw new BadAttributeException("Attribute 'isbn' cannot be empty");
            }
            listing.setISBN(isbn);
            map.put("isbn", isbn);
        }
        String title = (String) payload.get("title");
        if (title != null) {
            if (title.isBlank()) {
                throw new BadAttributeException("Attribute 'title' cannot be empty");
            }
            listing.setTitle(title);
            map.put("title", title);
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


        String author = (String) payload.get("author");
        if (author != null) {
            if (author.isBlank()) {
                throw new BadAttributeException("Attribute 'author' cannot be empty");
            }
            listing.setAuthor(author);
            map.put("author", author);
        }

        String publisher = (String) payload.get("publisher");
        if (publisher != null) {
            if (publisher.isBlank()) {
                throw new BadAttributeException("Attribute 'publisher' cannot be empty");
            }
            listing.setPublisher(publisher);
            map.put("publisher", publisher);
        }

        String description = (String) payload.get("description");
        if (description != null) {
            if (description.isBlank()) {
                throw new BadAttributeException("Attribute 'description' cannot be empty");
            }
            listing.setDescription(description);
            map.put("description", description);
        }

        Integer inventory = (Integer) payload.get("inventory");
        if (inventory != null) {
            if (inventory < 0) {
                throw new BadAttributeException("Attribute 'inventory' cannot be negative");
            }
            listing.setInventory(inventory);
            map.put("inventory", inventory);
        }
        String releaseDate = (String) payload.get("releaseDate");
        if (releaseDate != null) {
            if (releaseDate.isBlank()) {
                throw new BadAttributeException("Attribute 'releaseDate' cannot be empty");
            }
            listing.setReleaseDate(releaseDate);
            map.put("releaseDate", releaseDate);
        }

        String coverImage = (String) payload.get("coverImage");
        if (coverImage != null) {
            if (coverImage.isBlank()) {
                throw new BadAttributeException("Attribute 'coverImage' cannot be empty");
            }
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
