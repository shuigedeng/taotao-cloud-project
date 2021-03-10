package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.service;


import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.entity.Admin;
import java.util.List;

public interface AdminService {
    public Admin login(String username, String password);

    public List<Admin> showAllAdmin();
}
