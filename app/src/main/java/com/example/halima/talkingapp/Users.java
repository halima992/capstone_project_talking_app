package com.example.halima.talkingapp;

public class Users {
    private String fullname;
    private String username;

    public Users() {
    }

    public Users(String fullname,String username) {
        this.fullname = fullname;
        this.username=username;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String name) {
        this.fullname = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }


}
