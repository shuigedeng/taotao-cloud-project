package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.dao.impl;

import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.dao.AdminDao;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.entity.Admin;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.utils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class AdminDaoImpl implements AdminDao {
    private QueryRunner queryRunner = new QueryRunner();
    @Override
    public int insert(Admin admin) {
        return 0;
    }

    @Override
    public int delete(String username) {
        return 0;
    }

    @Override
    public int update(Admin admin) {
        return 0;
    }

    @Override
    public Admin select(String username) {
        try {
            Admin admin = queryRunner.query(DbUtils.getConnection(),"select * from admin where username=?;",new BeanHandler<Admin>(Admin.class),username);
            return admin;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Admin> selectAll() {
        try {
            List<Admin> admins = queryRunner.query(DbUtils.getConnection(),"select * from admin",new BeanListHandler<Admin>(Admin.class));
            return admins;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
