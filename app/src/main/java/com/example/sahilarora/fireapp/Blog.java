package com.example.sahilarora.fireapp;

/**
 * Created by sahilarora on 31/01/17.
 */

public class Blog {

    private String Description,Image,Title,username;


    public Blog(){


    }

    public Blog(String description, String image, String title) {
        Description = description;
        Image = image;
        Title = title;
        this.username = username;
    }

    public String getUsername(){
        return username;
    }


public void setUsername(String username){
    this.username=username;
}


    public String getDescription() {

        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
