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
            new Hotel(3L, "North Eye", "Sector 74, Near Grand Ajnara Heritage", null,
                    null, null,
                    null,
                    "NorthEye.jpeg")
    );

    @Override
    public List<Hotel> getAllHotels() {
        return hotels;
    }
}

