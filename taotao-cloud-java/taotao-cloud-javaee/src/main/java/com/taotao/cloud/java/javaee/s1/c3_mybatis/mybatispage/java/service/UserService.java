package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.service;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Page;
import com.qf.entity.User;

public interface UserService {
    // 分页查询service
    PageInfo<User> queryUsers(Page page);
}
