package com.example.myapplication.Models;

public class User {
    private String id;
    private String username;
    private String email;
    private String status;
    private String imageURL;
    private String numarMatricol;
    public String getNumarMatricol() {
        return numarMatricol;
    }

    public void setNumarMatricol(String numarMatricol) {
        this.numarMatricol = numarMatricol;
    }



    public User(String id, String username, String imageURL,String email,String status,String numarMatricol) {
        this.id = id;
        this.email=email;
        this.status=status;
        this.username = username;
        this.imageURL = imageURL;
        this.numarMatricol = numarMatricol;
    }
    public User(){
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getEmail(){return email;}

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

