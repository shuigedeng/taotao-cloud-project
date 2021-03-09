package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servletProject.dao.impl;

import com.qf.servletProject.dao.ManagerDao;
import com.qf.servletProject.entity.Manager;
import com.qf.servletProject.utils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class ManagerDaoImpl implements ManagerDao {
    private QueryRunner queryRunner = new QueryRunner();
    @Override
    public Manager select(String username) {
        try {
            Manager manager = queryRunner.query(DbUtils.getConnection(),"select * from manager where username=?",new BeanHandler<Manager>(Manager.class),username);
            return manager;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
