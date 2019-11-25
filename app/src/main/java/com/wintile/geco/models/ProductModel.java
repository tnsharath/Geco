package com.wintile.geco.models;

public class ProductModel {
    public String productName, category, imgUrl, shareUrl, desc, specs;
    public int produtID;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public ProductModel() {
    }

    public ProductModel(String productName, String category, String imgUrl, int produtID, String shareUrl, String desc, String specs) {
        this.productName = productName;
        this.category = category;
        this.imgUrl = imgUrl;
        this.produtID = produtID;
        this.shareUrl = shareUrl;
        this.desc = desc;
        this.specs = specs;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getProdutID() {
        return produtID;
    }

    public void setProdutID(int produtID) {
        this.produtID = produtID;
    }
}
