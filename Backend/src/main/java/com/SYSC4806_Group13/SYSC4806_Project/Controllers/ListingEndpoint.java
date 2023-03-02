package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.ListingNotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.ListingIdMissingException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Listing;
import com.SYSC4806_Group13.SYSC4806_Project.Model.ListingRepository;

import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Listing> getAllListings() {
        Iterable<Listing> iterable = repo.findAll();
        return Streamable.of(iterable).toList();
    }

    @GetMapping("/listings/{id}")
    public Listing getListing(@PathVariable String id) {
        Optional<Listing> listing = repo.findById(Long.parseLong(id));
        if (listing.isPresent()) {
            return listing.get();
        } else {
            throw new ListingNotFoundException();
        }
    }

    @PostMapping("/listings")
    public Map<String, Object> addListing(@RequestBody Map<String, Object> payload) {
        Map<String, Object> map = new HashMap<>();
        Listing listing = new Listing(
                (String) payload.get("isbn"),
                (String) payload.get("title"),
                Float.valueOf((String) payload.get("price")),
                (String) payload.get("author"),
                (String) payload.get("publisher"),
                (String) payload.get("description"),
                (int) payload.get("inventory"),
                (String) payload.get("releaseDate"),
                (boolean) payload.get("isActive"),
                (String) payload.get("coverImage")
        );

        repo.save(listing);

        map.put("listingId", listing.getListingId());

        return map;
    }

    @PutMapping("/listings")
    public Map<String, Object> putListing(@RequestBody Map<String, Object> payload) {
        String listingId = (String) payload.get("listingId");
        if (listingId == null) {
            throw new ListingIdMissingException();
        }

        Listing listing = repo.findByListingId(Long.parseLong(listingId));


        String isbn = (String) payload.get("isbn");
        System.out.println(isbn);
        if (isbn != null) {
            listing.setISBN(isbn);
        }
        String title = (String) payload.get("title");
        if (title != null) {
            listing.setTitle(title);
        }
        String price = (String) payload.get("price");
        if (price != null) {
            listing.setPrice(Float.valueOf(price));
        }
        String author = (String) payload.get("author");
        if (author != null) {
            listing.setAuthor(author);
        }
        String publisher = (String) payload.get("publisher");
        if (publisher != null) {
            listing.setPublisher(publisher);
        }
        String description = (String) payload.get("description");
        if (description != null) {
            listing.setDescription(description);
        }
        Integer inventory = (int) payload.get("inventory");
        // TODO
        if (inventory != null) {
            listing.setInventory(inventory);
        }
        String releaseDate = (String) payload.get("releaseDate");
        if (releaseDate != null) {
            listing.setReleaseDate(releaseDate);
        }
        Boolean isActive = (boolean) payload.get("isActive");
        if (isActive != null) {
            listing.setActive(isActive);
        }
        String coverImage = (String) payload.get("coverImage");
        if (coverImage != null) {
            listing.setCoverImage(coverImage);
        }
        repo.save(listing);
        Map<String, Object> map = new HashMap<>();
        return map;
    }


}
