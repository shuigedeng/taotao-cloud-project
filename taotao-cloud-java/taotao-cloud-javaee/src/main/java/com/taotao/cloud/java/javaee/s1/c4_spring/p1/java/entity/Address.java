package com.taotao.cloud.java.javaee.s1.c4_spring.p1.java.entity;

public class Address {
    private Integer id;
    private String city;

    public Address(){
        System.out.println("Addres 构造方法");
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        System.out.println("Address SetId");
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void init_qf(){
        System.out.println("Address 初始化");
    }

    public void destroy_qf(){
        System.out.println("Address 销毁");
    }
}
