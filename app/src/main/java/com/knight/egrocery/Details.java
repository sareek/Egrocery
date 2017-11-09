package com.knight.egrocery;


public class Details {
    private String name, thumbnailUrl;
    private int price,Id;



    public Details() {
    }

    public Details(int Id,String name, String thumbnailUrl, int price) {
        this.Id=Id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.price = price;



    }
    public int getId() {
        return Id;
    }

    public void setId(int Id ) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}