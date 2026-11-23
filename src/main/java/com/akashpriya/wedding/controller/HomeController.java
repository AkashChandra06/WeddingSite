package com.akashpriya.wedding.controller;

import com.akashpriya.wedding.config.WeddingProperties;
import com.akashpriya.wedding.constants.AppConstants;
import com.akashpriya.wedding.service.EventService;
import com.akashpriya.wedding.service.FamilyService;
import com.akashpriya.wedding.service.GalleryService;
import com.akashpriya.wedding.service.HotelService;
import com.akashpriya.wedding.service.MemoryWallService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;

/**
 * Serves the single-page public wedding website with all sections.
 */
@Controller
public class HomeController {

    private static final String MUSIC_CLASSPATH_LOCATION = "static/audio/wedding-instrumental.mp3";

    private final EventService eventService;
    private final HotelService hotelService;
    private final GalleryService galleryService;
    private final FamilyService familyService;
    private final MemoryWallService memoryWallService;
    private final WeddingProperties weddingProperties;

    public HomeController(EventService eventService, HotelService hotelService, GalleryService galleryService,
                           FamilyService familyService, MemoryWallService memoryWallService,
                           WeddingProperties weddingProperties) {
        this.eventService = eventService;
        this.hotelService = hotelService;
        this.galleryService = galleryService;
        this.familyService = familyService;
        this.memoryWallService = memoryWallService;
        this.weddingProperties = weddingProperties;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("galleryImages", galleryService.getAllImages());

        model.addAttribute("akashFamilyIntro", familyService.getIntroductionMembers(AppConstants.SIDE_AKASH));
        model.addAttribute("priyaFamilyIntro", familyService.getIntroductionMembers(AppConstants.SIDE_PRIYA));

        model.addAttribute("akashFamilyTree", familyService.getFamilyTree(AppConstants.SIDE_AKASH));
        model.addAttribute("priyaFamilyTree", familyService.getFamilyTree(AppConstants.SIDE_PRIYA));

        model.addAttribute("memoryPosts", memoryWallService.getApprovedPosts());

        String weddingDateIso = weddingProperties.getDate() != null
                ? weddingProperties.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null;
        model.addAttribute("weddingDateIso", weddingDateIso);
        model.addAttribute("weddingDateDisplay", weddingProperties.getDate());

        // Music toggle is only rendered if the optional instrumental track actually exists,
        // so the UI never shows a control for a missing/broken audio file.
        model.addAttribute("musicAvailable", new ClassPathResource(MUSIC_CLASSPATH_LOCATION).exists());

        return "index";
    }
}



