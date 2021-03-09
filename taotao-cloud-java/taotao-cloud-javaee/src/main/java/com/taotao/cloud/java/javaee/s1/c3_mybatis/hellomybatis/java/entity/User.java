package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity;

import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String password;
    private Boolean gender;
    private Date registTime;

    public User(){}
    public User(Integer id, String username, String password, Boolean gender, Date registTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.registTime = registTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", registTime=" + registTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }
}
