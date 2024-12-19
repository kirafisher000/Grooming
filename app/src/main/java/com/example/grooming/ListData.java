package com.example.grooming;

public class ListData {
    String title,price;
    int image;

    public ListData(int image, String price, String title) {
        this.image = image;
        this.price = price;
        this.title = title;
    }

    public void setData(int image, String price, String title){
        this.image = image;
        this.price = price;
        this.title = title;
    }

    public int getImage(){
        return this.image;
    }

    public String getPrice(){
        return this.price;
    }

    public String getTitle(){
        return this.title;
    }
}
