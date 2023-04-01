package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.*;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import static java.lang.Long.parseLong;

@RestController
public class RecommendationController {

    private final double JACCARD_THRESHOLD = 0.2;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepo;
    private final ListingRepository listingRepository;

    public RecommendationController(UserRepository userRepository, CartItemRepository cartItemRepo,
                                    ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.cartItemRepo = cartItemRepo;
        this.listingRepository = listingRepository;
    }

    private double jaccardSimilarity(Set<Long> a, Set<Long> b) {
        final int sa = a.size();
        final int sb = b.size();
        a.retainAll(b);
        final int intersection = a.size();
        return 1d / (sa + sb - intersection) * intersection;
    }

    @GetMapping("/recommendation/{userId}")
    @ResponseBody()
    public Set<Listing> getRecommendedBooks(@PathVariable String userId) {
        // get selected user order history
        User user = userRepository.findUserById(parseLong(userId));
        List<CartItem> selectedUserOrderHistory = cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(user.getId());

        // get users who have order history and are not selected user
        List<User> allUsers = (List<User>) userRepository.findAll();
        List<User> filteredUsers = allUsers.stream()
                .filter(u -> !Objects.equals(u.getId(), user.getId()))
                .filter(u -> !cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(u.getId()).isEmpty())
                .toList();

        // get selected user listing ids
        Set<Long> user1OrderListingId = new HashSet<>();
        for (CartItem c: selectedUserOrderHistory) {
            user1OrderListingId.add(c.getListing().getListingId());
        }

        // iterate filtered users to find users who have similar order history according to jaccard index threshold
        Set<Long> recommendedBookIds = new HashSet<>();
        for (User u: filteredUsers) {
            List<CartItem> similarUserOrderHistory = cartItemRepo.findAllByUserIdAndPurchaseDateTimeIsNotNullOrderByPurchaseDateTimeDesc(u.getId());

            // get listing ids
            Set<Long> user2OrderListingId = new HashSet<>();
            for (CartItem c: similarUserOrderHistory) {
                user2OrderListingId.add(c.getListing().getListingId());
            }

            double jaccard = jaccardSimilarity(user1OrderListingId, user2OrderListingId);
            if (jaccard > JACCARD_THRESHOLD) {
                user2OrderListingId.removeAll(user1OrderListingId);
                recommendedBookIds.addAll(user2OrderListingId);
            }
        }

        // convert recommended listing id to Listing objects
        Set<Listing> recommendedBooks = new HashSet<>();
        for (Long listingId: recommendedBookIds) {
            Listing l = listingRepository.findByListingId(listingId);
            recommendedBooks.add(l);
        }
        return recommendedBooks;
    }
}
