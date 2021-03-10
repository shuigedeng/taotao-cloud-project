package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.dao.impl;

import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.dao.EmpManagerDao;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.entity.EmpManager;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.utils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class EmpManagerDaoImpl implements EmpManagerDao {
    private QueryRunner queryRunner = new QueryRunner();
    @Override
        public EmpManager select(String username) {
            try {
                EmpManager empManager = queryRunner.query(DbUtils.getConnection(),"select * from empmanager where username = ?;",new BeanHandler<EmpManager>(EmpManager.class),username);
                return empManager;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
    }
}
