package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.entity;

public class User {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        System.out.println("set User");
        this.id = id;
    }
    public User(){
        System.out.println("构造User");
    }

    public void initUser(){
        System.out.println("初始化User");
    }
}
