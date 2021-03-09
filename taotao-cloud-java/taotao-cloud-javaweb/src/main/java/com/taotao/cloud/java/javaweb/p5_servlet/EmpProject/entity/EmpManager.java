package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.entity;

public class EmpManager {
    private String username;
    private String password;

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

    public EmpManager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public EmpManager() {
    }
}
