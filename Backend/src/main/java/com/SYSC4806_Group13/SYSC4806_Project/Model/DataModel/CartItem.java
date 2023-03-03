package com.SYSC4806_Group13.SYSC4806_Project.Model;
import jakarta.persistence.*;
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long userID;
    private Long listingID;
    private Long quantity;

    public CartItem(){}
    public CartItem(Long userID, Long listingID, Long quantity) {
        this.userID = userID;
        this.listingID = listingID;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserID() { return userID;}
    public void setUserID(Long userID) { this.userID = userID;}
    public Long getListingID() { return listingID; }
    public void setListingID(Long listingID) { this.listingID = listingID;}
    public Long getQuantity() {return quantity;}
    public void setQuantity(Long quantity) {this.quantity = quantity;}
}
