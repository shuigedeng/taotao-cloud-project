package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity;

import java.util.List;

public class Subject {
    private Integer id;
    private String name;
    private Integer grade;

    private List<Student2> students;
    public Subject(){}
    public Subject(Integer id, String name, Integer grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }

    public List<Student2> getStudents() {
        return students;
    }

    public void setStudents(List<Student2> students) {
        this.students = students;
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
