package com.pheonix.org.hallaka.Models;

public class SalonDataModel {

    String name, city, image, id, ownerId;

    public SalonDataModel() {
    }

    public SalonDataModel(String name, String city, String image, String id, String ownerId) {
        this.name = name;
        this.city = city;
        this.image = image;
        this.id = id;
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
