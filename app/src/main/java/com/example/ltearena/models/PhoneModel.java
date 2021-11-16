package com.example.ltearena.models;

public class PhoneModel {

    private String phoneName, image, detailUrl, slug;

    public PhoneModel() {

    }

    public PhoneModel(String phoneName, String image, String detailUrl, String slug) {
        this.phoneName = phoneName;
        this.image = image;
        this.detailUrl = detailUrl;
        this.slug = slug;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
