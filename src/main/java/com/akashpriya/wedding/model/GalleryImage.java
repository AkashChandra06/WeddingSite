package com.akashpriya.wedding.model;

/**
 * A single photo entry for the gallery (in-memory sample data, not persisted).
 */
public class GalleryImage {

    private Long id;
    private String imagePath;
    private String category;
    private String caption;

    public GalleryImage() {
    }

    public GalleryImage(Long id, String imagePath, String category, String caption) {
        this.id = id;
        this.imagePath = imagePath;
        this.category = category;
        this.caption = caption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

