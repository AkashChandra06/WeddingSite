package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.model.Hotel;
import com.akashpriya.wedding.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    private final List<Hotel> hotels = List.of(
            new Hotel(1L, "Nimantrana", "Spectrum Mall, Sector 75", null,
                    null, null,
                    null,
                    "Nimantrana.avif"),
            new Hotel(2L, "Hardik Palace", "Sector 116, Near Fiaba", null,
                    null, null,
                    null,
                    "Hardik Place.jpg"),
            new Hotel(3L, "Coming Soon", "", null,
                    null, null,
                    null,
                    "hotel-placeholder-3.jpg")
    );

    @Override
    public List<Hotel> getAllHotels() {
        return hotels;
    }
}

