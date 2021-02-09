package com.example.smartcall;

public class RecyclerItem {
    private int profileImg, ratingImg, whatsappContactIndex;
    private String name_surname;

    public RecyclerItem(int profImgRes, int ratingImgRes, String text, int index){
        profileImg = profImgRes;
        ratingImg = ratingImgRes;
        name_surname = text;
        whatsappContactIndex = index;
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
    public int getWhatsappContactIndex() {
        return whatsappContactIndex;
    }

    public void clicked(String text){
        name_surname = text;
    }
}
