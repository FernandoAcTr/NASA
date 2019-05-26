package model;

import java.sql.Blob;

public class User {
    private int id;
    private String username;
    private String password;
    private String type;
    private MyImage imageProfile;
    private MyImage imageCover;

    public  static final String TYPE_ADMIN = "admin";
    public  static final String TYPE_USER = "user";

    public User() {
    }

    public User(int id, String username, String password, String type, MyImage imageProfile, MyImage imageCover) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MyImage getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(MyImage imageProfile) {
        this.imageProfile = imageProfile;
    }

    public MyImage getImageCover() {
        return imageCover;
    }

    public void setImageCover(MyImage imageCover) {
        this.imageCover = imageCover;
    }
}
