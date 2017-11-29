package com.example.roshan.berlin.model;

/**
 * Created by roshan on 11/7/17.
 */

public class MenuType {
    String title;
    int image;
    String desc;

    public MenuType(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public MenuType(String title, int image, String desc) {
        this.title = title;
        this.image = image;
        this.desc = desc;
    }
}
