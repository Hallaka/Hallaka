package com.pheonix.org.hallaka.Models;

public class BookingDataModel {
    String date, time, barberName, customerName, saloonName,barberPhone, sId, bId, uid, status, price,id;

    public BookingDataModel() {
    }

    public BookingDataModel(String date, String time, String barberName, String customerName, String saloonName, String barberPhone, String sId, String bId, String uid, String status, String price, String id) {
        this.date = date;
        this.time = time;
        this.barberName = barberName;
        this.customerName = customerName;
        this.saloonName = saloonName;
        this.barberPhone = barberPhone;
        this.sId = sId;
        this.bId = bId;
        this.uid = uid;
        this.status = status;
        this.price = price;
        this.id = id;
    }

    public String getBarberPhone() {
        return barberPhone;
    }

    public void setBarberPhone(String barberPhone) {
        this.barberPhone = barberPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSaloonName() {
        return saloonName;
    }

    public void setSaloonName(String saloonName) {
        this.saloonName = saloonName;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
