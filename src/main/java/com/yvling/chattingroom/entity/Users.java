package com.yvling.chattingroom.entity;

public class Users {
    private Integer user_id;
    private String user_name;
    private String user_password;
    private String user_phone;

    public Users(Integer user_id, String user_name, String user_password, String user_phone) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_phone = user_phone;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    @Override
    public String toString() {
        return "Users{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_phone='" + user_phone + '\'' +
                '}';
    }
}
