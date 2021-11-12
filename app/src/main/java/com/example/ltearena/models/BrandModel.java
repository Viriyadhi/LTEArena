package com.example.ltearena.models;

public class BrandModel {
    private int brandId;
    private String brandName, detail;

    public BrandModel(int brandId, String brandName, String detail) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.detail = detail;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
