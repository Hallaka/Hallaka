package com.pheonix.org.hallaka.Models;

public class BarberDataModel {

    String name,rate,image,sTime,cTime,id;

    public BarberDataModel() {
    }

    public BarberDataModel(String name, String rate, String image, String sTime, String cTime, String id) {
        this.name = name;
        this.rate = rate;
        this.image = image;
        this.sTime = sTime;
        this.cTime = cTime;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
