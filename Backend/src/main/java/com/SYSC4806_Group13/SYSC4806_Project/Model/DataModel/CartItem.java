package com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long quantity;


    @ManyToOne(targetEntity = Listing.class)
    private Listing listing;

    public CartItem() {
    }

    public CartItem(Long userId, Listing listing, Long quantity) {
        this.userId = userId;
        this.listing = listing;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userID) {
        this.userId = userID;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

}
