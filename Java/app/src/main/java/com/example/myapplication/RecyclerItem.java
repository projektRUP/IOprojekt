package com.example.myapplication;

public class RecyclerItem {
    private int profileImg, ratingImg;
    private String name_surname;

    public RecyclerItem(int profImgRes, int ratingImgRes, String text){
        profileImg = profImgRes;
        ratingImg = ratingImgRes;
        name_surname = text;
    }

    public int getProfImg(){
        return profileImg;
    }
    public int getRatingImg(){
        return ratingImg;
    }
    public String getNameSurname(){
        return name_surname;
    }

    public void clicked(String text){
        name_surname = text;
    }
}
