package com.taotao.cloud.java.javaee.s1.c5_springmvc.p1.java.entity;

import java.util.List;

public class UserList {
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    private List<User> users;
}
