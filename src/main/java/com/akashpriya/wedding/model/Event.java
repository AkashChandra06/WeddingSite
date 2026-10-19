package com.akashpriya.wedding.model;

import jakarta.persistence.*;

/**
 * Represents a single event in the wedding schedule (Haldi, Mehendi, Wedding, Reception, etc.).
 */
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date;
    private String time;
    private String venue;
    private String address;
    private String dressCode;

    @Column(length = 1000)
    private String description;

    private String mapLink;
    private String imageName;

    public Event() {
    }

    public Event(Long id, String name, String date, String time, String venue, String address,
                 String dressCode, String description, String mapLink, String imageName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.address = address;
        this.dressCode = dressCode;
        this.description = description;
        this.mapLink = mapLink;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

