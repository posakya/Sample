package com.example.roshan.berlin.model;

/**
 * Created by roshan on 11/23/17.
 */

public class GalleryModel {


    String id;
    String image;

    public GalleryModel(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public GalleryModel(){
        //empty constructor
    }

}
