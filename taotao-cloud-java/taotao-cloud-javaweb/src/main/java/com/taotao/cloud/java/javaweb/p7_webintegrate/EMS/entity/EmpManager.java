package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.entity;

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

    public EmpManager() {
    }

    public EmpManager(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
