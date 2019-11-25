package com.wintile.geco.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleItemModel implements Parcelable{

    public static final Parcelable.Creator<SingleItemModel> CREATOR = new Parcelable.Creator<SingleItemModel>() {
        @Override
        public SingleItemModel createFromParcel(Parcel in) {
            return new SingleItemModel(in);
        }

        @Override
        public SingleItemModel[] newArray(int size) {
            return new SingleItemModel[size];
        }
    };
    public String name, url,  category, shareUrl, desc, specs;
    public int produtID;
    public SingleItemModel() {

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getProdutID() {
        return produtID;
    }

    public void setProdutID(int produtID) {
        this.produtID = produtID;
    }

    public SingleItemModel(String name, String url, String cat, int productID, String shareUrl, String desc, String specs) {
        this.name = name;
        this.url = url;
        this.category = cat;
        this.produtID = productID;
        this.shareUrl = shareUrl;
        this.desc = desc;
        this.specs = specs;
    }
    protected SingleItemModel(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
