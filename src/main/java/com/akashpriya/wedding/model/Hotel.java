package com.akashpriya.wedding.model;

import jakarta.persistence.*;

/**
 * Recommended hotel for out-of-town guests.
 */
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String distanceFromVenue;
    private String contactNumber;
    private String mapLink;

    @Column(length = 1000)
    private String bookingNote;

    private String imageName;

    public Hotel() {
    }

    public Hotel(Long id, String name, String address, String distanceFromVenue, String contactNumber,
                 String mapLink, String bookingNote, String imageName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distanceFromVenue = distanceFromVenue;
        this.contactNumber = contactNumber;
        this.mapLink = mapLink;
        this.bookingNote = bookingNote;
        this.imageName = imageName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistanceFromVenue() {
        return distanceFromVenue;
    }

    public void setDistanceFromVenue(String distanceFromVenue) {
        this.distanceFromVenue = distanceFromVenue;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getBookingNote() {
        return bookingNote;
    }

    public void setBookingNote(String bookingNote) {
        this.bookingNote = bookingNote;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

