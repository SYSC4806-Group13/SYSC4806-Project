package com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Listing listing;
    
    private Timestamp purchaseDateTime;

    private Float totalCartItemPriceAtPurchase;

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

    public Timestamp getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(Timestamp purchaseDate) {
        this.purchaseDateTime = purchaseDate;
    }

    public Float getTotalCartItemPriceAtPurchase() {
        return totalCartItemPriceAtPurchase;
    }

    public void setTotalCartItemPriceAtPurchase(Float totalCartItemPriceAtPurchase) {
        this.totalCartItemPriceAtPurchase = totalCartItemPriceAtPurchase;
    }

    public boolean checkout() {
        if (purchaseDateTime == null && totalCartItemPriceAtPurchase == null && quantity <= listing.getInventory()) {
            purchaseDateTime = new Timestamp(System.currentTimeMillis());
            totalCartItemPriceAtPurchase = quantity * (listing.getPrice() == null ? 0.01f : listing.getPrice());
            listing.setInventory((int) (listing.getInventory() - quantity));
            return true;
        } else {
            return false;
        }
    }
}
