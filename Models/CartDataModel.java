package com.pheonix.org.hallaka.Models;

public class CartDataModel {

    String uid,oid,id,productName,productImage,tPrice,quantity,status,buyerPhone;


    public CartDataModel(String uid, String oid, String id, String productName, String productImage, String tPrice, String quantity, String status, String buyerPhone) {
        this.uid = uid;
        this.oid = oid;
        this.id = id;
        this.productName = productName;
        this.productImage = productImage;
        this.tPrice = tPrice;
        this.quantity = quantity;
        this.status = status;
        this.buyerPhone = buyerPhone;
    }



    public String getBuyerPhone() {
        return buyerPhone;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CartDataModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String gettPrice() {
        return tPrice;
    }

    public void settPrice(String tPrice) {
        this.tPrice = tPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
