package com.example.ltearena.models;

public class PhoneModel {

    private String phoneName, image, detailUrl;

    public PhoneModel(String phoneName, String image, String detailUrl) {
        this.phoneName = phoneName;
        this.image = image;
        this.detailUrl = detailUrl;
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
}
