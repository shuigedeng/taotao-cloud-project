package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.service.impl;


import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.dao.AdminDao;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.dao.impl.AdminDaoImpl;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.entity.Admin;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.service.AdminService;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.utils.DbUtils;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao = new AdminDaoImpl();
    @Override
    public Admin login(String username, String password) {
        Admin result = null;
        try {
            DbUtils.begin();
            Admin admin = adminDao.select(username);
            if(admin!=null){
                if(admin.getPassword().equals(password)){
                    result = admin;
                }
            }
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Admin> showAllAdmin() {
        List<Admin> admins = null;
        try {
            DbUtils.begin();
            admins = adminDao.selectAll();
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }

        return admins;
    }
}
