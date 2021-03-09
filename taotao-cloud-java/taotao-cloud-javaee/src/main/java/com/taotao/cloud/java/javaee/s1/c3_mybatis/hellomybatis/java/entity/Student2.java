package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity;

import java.util.List;

public class Student2 {
    private Integer id;
    private String name;
    private Boolean sex;

    private List<Subject> subjects;
    public Student2(){}
    public Student2(Integer id, String name, Boolean sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
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
}
