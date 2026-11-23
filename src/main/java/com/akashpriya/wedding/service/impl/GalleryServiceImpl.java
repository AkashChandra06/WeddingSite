package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.constants.AppConstants;
import com.akashpriya.wedding.model.GalleryImage;
import com.akashpriya.wedding.service.GalleryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryServiceImpl implements GalleryService {

    private final List<GalleryImage> images = List.of(
            new GalleryImage(1L, "/images/gallery/photo1.jpg", AppConstants.CATEGORY_OUR_STORY, "Where it all began"),
            new GalleryImage(2L, "/images/gallery/photo2.jpg", AppConstants.CATEGORY_OUR_STORY, "Our first trip together"),
            new GalleryImage(3L, "/images/gallery/photo3.jpg", AppConstants.CATEGORY_FAMILY, "Family gathering"),
            new GalleryImage(4L, "/images/gallery/photo4.jpg", AppConstants.CATEGORY_FAMILY, "Sunday lunch with family"),
            new GalleryImage(5L, "/images/gallery/photo5.jpg", AppConstants.CATEGORY_WEDDING_MOMENTS, "Engagement day"),
            new GalleryImage(6L, "/images/gallery/photo6.jpg", AppConstants.CATEGORY_WEDDING_MOMENTS, "Pre-wedding shoot")
    );

    @Override
    public List<GalleryImage> getAllImages() {
        return images;
    }
}

