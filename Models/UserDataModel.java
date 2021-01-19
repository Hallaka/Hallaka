package com.pheonix.org.hallaka.Models;

public class UserDataModel {

    String name,pass,email,phone,address,uid,type,img;

    public UserDataModel() {
    }

    public UserDataModel(String name, String pass, String email, String phone, String address, String uid, String type, String img) {
        this.name = name;
        this.pass = pass;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.uid = uid;
        this.type = type;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
