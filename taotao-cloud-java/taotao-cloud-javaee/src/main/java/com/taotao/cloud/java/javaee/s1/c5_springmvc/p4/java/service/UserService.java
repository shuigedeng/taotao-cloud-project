package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.service;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Page;
import com.qf.entity.User;

import java.util.List;

public interface UserService {
    PageInfo<User> queryUsers(Page page);
}
