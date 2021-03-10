package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.entity;

public class Manager {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "Manager{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
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

    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Manager() {
    }
}
