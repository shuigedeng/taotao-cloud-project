package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qf.serialize.MySerializer;

import java.util.Date;
import java.util.List;

public class User {
    @JsonProperty("id2")
    private Integer id;
    @JsonIgnore
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date birth;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> hobby;// null   size=0

    @JsonSerialize(using = MySerializer.class)
    private Double salary = 10000.126;//在输出此属性时，使用MySerializer输出

    public User(){}

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Integer id, String name, List<String> hobby) {
        this.id = id;
        this.name = name;
        this.hobby = hobby;
    }

    public User(int id, String name, Date date) {
        this.id=id;
        this.name=name;
        this.birth=date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                '}';
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
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

    public List<String> getHobby() {
        return hobby;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }
}
