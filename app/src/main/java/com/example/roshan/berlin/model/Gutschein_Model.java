package com.example.roshan.berlin.model;


/**
 * Created by roshan on 11/15/17.
 */

public class Gutschein_Model {
    private String voucher_image;
    private String textPrice;
    private String discountPrice;

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Gutschein_Model(){
        //ecmpty cunstroctor
    }

    public Gutschein_Model(String voucher_image, String textPrice) {
        this.voucher_image = voucher_image;
        this.textPrice = textPrice;
    }

    public String getVoucher_image() {
        return voucher_image;
    }

    public void setVoucher_image(String voucher_image) {
        this.voucher_image = voucher_image;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice = textPrice;
    }

}
