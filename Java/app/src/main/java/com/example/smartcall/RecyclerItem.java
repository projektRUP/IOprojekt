package com.example.smartcall;

import android.graphics.Bitmap;

public class RecyclerItem {
    private int ratingImg, whatsappContactIndex;
    private String name_surname;
    Bitmap profileImg;

    public RecyclerItem(Bitmap profImgRes, int ratingImgRes, String text, int index){
        profileImg = profImgRes;
        ratingImg = ratingImgRes;
        name_surname = text;
        whatsappContactIndex = index;
    }

    public Bitmap getProfImg(){
        return profileImg;
    }
    public int getRatingImg(){
        return ratingImg;
    }
    public String getNameSurname(){
        return name_surname;
    }
    public int getWhatsappContactIndex() {
        return whatsappContactIndex;
    }

    public void clicked(String text){
        name_surname = text;
    }
}
