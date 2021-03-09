package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servletProject.service;

import com.qf.servletProject.entity.Admin;

import java.util.List;

public interface AdminService {
    public Admin login(String username, String password);

    public List<Admin> showAllAdmin();
}
