package com.example.grooming;

public class ListAdmin {
    String phone,title,price,date,time;
    int id;

    public ListAdmin(int id,String phone, String price, String title,String date, String time) {
        this.phone = phone;
        this.price = price;
        this.title = title;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getDate(){
        return this.date;
    }

    public int getId(){
        return this.id;
    }

    public String getTime(){
        return this.time;
    }

    public String getPrice(){
        return this.price;
    }

    public String getTitle(){
        return this.title;
    }
}
