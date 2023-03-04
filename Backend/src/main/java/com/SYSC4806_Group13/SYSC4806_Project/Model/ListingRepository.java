package com.SYSC4806_Group13.SYSC4806_Project.Model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListingRepository extends CrudRepository<Listing, Long> {
    Listing findByListingId(long listingId);
    List<Listing> findAllBySellerUserId(long sellerUserId);
}
