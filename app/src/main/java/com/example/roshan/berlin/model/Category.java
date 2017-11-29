package com.example.roshan.berlin.model;

/**
 * Created by roshan on 10/8/17.
 */

public class Category {

    public Category(){
        //empty constructor
    }

    int category_image;
    int category_name;
    String category_detail;

    public Category(int category_image, int category_name, String category_detail) {
        this.category_image = category_image;
        this.category_name = category_name;
        this.category_detail = category_detail;
    }

    public int getCategory_image() {
        return category_image;
    }

    public void setCategory_image(int category_image) {
        this.category_image = category_image;
    }

    public int getCategory_name() {
        return category_name;
    }

    public void setCategory_name(int category_name) {
        this.category_name = category_name;
    }

    public String getCategory_detail() {
        return category_detail;
    }

    public void setCategory_detail(String category_detail) {
        this.category_detail = category_detail;
    }
}
