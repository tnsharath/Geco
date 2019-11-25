package com.wintile.geco.models;

/**
 * Created by Sharath Kumar T N on 08-10-2018.
 */
public class GridItem {
    private String image;
    private String title;

    public GridItem() {
        super();
    }

    public GridItem(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}