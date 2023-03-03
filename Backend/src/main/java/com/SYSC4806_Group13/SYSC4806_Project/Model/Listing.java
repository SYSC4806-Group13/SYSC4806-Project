package com.SYSC4806_Group13.SYSC4806_Project.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long listingId;

    // TODO
    private Long sellerUserId;
    private String ISBN;
    private String title;
    private Float price;
    private String author;
    private String publisher;
    private String description;
    private Integer inventory;
    private String releaseDate;
    private Boolean isActive;

    @Column(nullable = true, length = 64)
    private String coverImage;

    public Listing(Long sellerUserId,
                   String ISBN,
                   String title,
                   Float price,
                   String author,
                   String publisher,
                   String description,
                   Integer inventory,
                   String releaseDate,
                   Boolean isActive,
                   String coverImage) {
        this.sellerUserId = sellerUserId;
        this.ISBN = ISBN;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.inventory = inventory;
        this.releaseDate = releaseDate;
        this.isActive = isActive;
        this.coverImage = coverImage;
    }

    public Listing() {

    }

    public Long getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(long sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
