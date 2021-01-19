package com.pheonix.org.hallaka.Models;

import android.net.Uri;

public class ProductDataModel {

    String name,price,image,uid,pId,tag;

    public ProductDataModel() {
    }

    public ProductDataModel(String name, String price, String image, String uid, String pId, String tag) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.uid = uid;
        this.pId = pId;
        this.tag = tag;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
