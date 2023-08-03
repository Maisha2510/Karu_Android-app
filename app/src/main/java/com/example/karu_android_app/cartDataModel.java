package com.example.karu_android_app;

public class cartDataModel {
    private String title;
    private int count;
    private double price;
    private String imageUrl;

    public cartDataModel() {
    }

    public cartDataModel(String title, int count, double price, String imageUrl) {
        this.title = title;
        this.count = count;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
