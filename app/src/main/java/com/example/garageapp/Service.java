package com.example.garageapp;

public class Service {
    private String name;
    private String price;
    private String estimatedTime;
    private String image;

    public Service(String name, String price, String estimatedTime, String image) {
        this.name = name;
        this.price = price;
        this.estimatedTime = estimatedTime;
        this.image = "image";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}