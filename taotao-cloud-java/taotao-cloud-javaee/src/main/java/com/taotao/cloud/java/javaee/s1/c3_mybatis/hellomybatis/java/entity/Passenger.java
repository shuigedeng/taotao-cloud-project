package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity;

import java.util.Date;

public class Passenger {
    private Integer id;
    private String name;
    private Boolean sex;
    private Date birthday;

    // 存储旅客的护照信息 ： 关系属性
    private Passport passport;

    public Passenger(){}
    public Passenger(Integer id, String name, Boolean sex, Date birthday) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
