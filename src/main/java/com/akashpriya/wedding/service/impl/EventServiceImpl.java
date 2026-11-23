package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.model.Event;
import com.akashpriya.wedding.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final List<Event> events = List.of(
            new Event(1L, "Bride Haldi", "28 November 2026", "11:00 AM",
                    "Bride's Residence", "Sector 2, Noida",
                    "Yellow / Traditional",
                    "A joyful beginning filled with colors, laughter, and blessings for Priya.",
                    null,
                    "Haldi Bride.png"),
            new Event(2L, "Groom Haldi", "29 November 2026", "11:00 AM",
                    "Groom's Residence", "Grand Ajnara Heritage",
                    "Yellow / Traditional",
                    "A joyful beginning filled with colors, laughter, and blessings for Akash.",
                    null,
                    "Haldi Groom.png"),
            new Event(3L, "Bride Mehendi", "29 November 2026", "6:00 PM",
                    "Bride's Residence", "Sector 2, Noida",
                    "Green / Festive",
                    "An evening of intricate henna art, music, and dance with loved ones.",
                    null,
                    "Mehendi Bride.png"),
            new Event(4L, "Tilak Ceremony", "30 November 2026", "11:00 AM",
                    "Groom's Residence", "Grand Ajnara Heritage",
                    "Traditional Attire",
                    "A sacred ceremony marking the formal blessing of the groom by the bride's family.",
                    null,
                    "Tilak Groom.png"),
            new Event(5L, "Engagement Ceremony", "30 November 2026", "7:00 PM",
                    "Adiraa 52", "Adiraa 52, Noida Sector 52 Metro",
                    "Formal / Evening Wear",
                    "The joyous exchange of rings, celebrating the promise of forever.",
                    "https://maps.google.com/?q=Adiraa+52+Noida+Sector+52+Metro",
                    "Engagement.png"),
            new Event(6L, "Wedding Ceremony", "1 December 2026", "7:00 PM",
                    "Adiraa", "Adiraa, Botanical Garden Metro",
                    "Traditional Indian Attire",
                    "The sacred union of Akash & Priya, blessed by family and friends.",
                    "https://maps.google.com/?q=Adiraa+Botanical+Garden+Metro",
                    "Wedding.png"),
            new Event(7L, "Reception", "20 December 2026", "7:00 PM",
                    "Kingship by Kahlon", "Vrindavan Society, Lucknow",
                    "Formal / Evening Wear",
                    "Celebrate the newly married couple at a grand reception filled with joy, music, and blessings.",
                    "https://maps.google.com/?q=Kingship+by+Kahlon+Vrindavan+Society+Lucknow",
                    "Reception.png")
    );

    @Override
    public List<Event> getAllEvents() {
        return events;
    }
}


