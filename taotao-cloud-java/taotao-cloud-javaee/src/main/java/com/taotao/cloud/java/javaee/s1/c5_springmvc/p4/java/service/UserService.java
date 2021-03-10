package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.service;

import com.github.pagehelper.PageInfo;

import com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.entity.Page;
import com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.entity.User;
import java.util.List;

public interface UserService {
    PageInfo<User> queryUsers(Page page);
}
