package com.example.zeynep.nativenotes;

import java.util.List;

public class UserModel {

    private int ID;
    private String userName;
    private String name;
    private String password;
    private String img;
    private List<String> noteIdList;

    public UserModel() {}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

//    public List<String> getNoteIdList() {
//        return noteIdList;
//    }
//
//    public void setNoteIdList(List<String> noteIdList) {
//        this.noteIdList = noteIdList;
//    }

    @Override
    public String toString() {
        return "UserModel{" + "ID=" + ID + ", userName='" + userName + '\'' + ", name='" + name + '\'' + ", password='" + password + '\'' + ", img='" + img + '\'' + ", noteIdList=" + noteIdList + '}';
    }
}
