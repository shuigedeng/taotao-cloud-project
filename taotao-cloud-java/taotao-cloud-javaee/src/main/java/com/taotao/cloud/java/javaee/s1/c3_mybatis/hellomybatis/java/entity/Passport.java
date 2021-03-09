package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity;

import java.util.Date;

public class Passport {
    private Integer id;
    private String nationality;
    private Date expire;

    // 存储旅客信息 ： 关系属性
    private Passenger passenger;

    public Passport(){}
    public Passport(Integer id, String nationality, Date expire) {
        this.id = id;
        this.nationality = nationality;
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "id=" + id +
                ", nationality='" + nationality + '\'' +
                ", expire=" + expire +
                '}';
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}
