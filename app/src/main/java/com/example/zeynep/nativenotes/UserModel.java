package com.example.zeynep.nativenotes;

import java.util.List;
import java.util.UUID;

public class UserModel {

    private String ID;
    private String userName;
    private String name;
    private String password;
    private String img;
    private String note_id;

    private int note_uniq_id;
    private String note;
    private String user_id_from_notes;

    public UserModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.ID = UUID.randomUUID().toString();
    }

    public UserModel() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public int getNote_uniq_id() {
        return note_uniq_id;
    }

    public void setNote_uniq_id(int note_uniq_id) {
        this.note_uniq_id = note_uniq_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUser_id_from_notes() {
        return user_id_from_notes;
    }

    public void setUser_id_from_notes(String user_id_from_notes) {
        this.user_id_from_notes = user_id_from_notes;
    }

    @Override
    public String toString() {
        return "UserModel{" + "ID=" + ID + ", userName='" + userName + '\'' + ", name='" + name + '\'' + ", password='" + password + '\'' + ", img='" + img + '\''  + '}';
    }
}
