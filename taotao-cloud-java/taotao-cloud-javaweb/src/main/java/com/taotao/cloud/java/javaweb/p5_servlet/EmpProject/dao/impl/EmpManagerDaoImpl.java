package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.dao.impl;

import com.qf.emp.dao.EmpManagerDao;
import com.qf.emp.entity.EmpManager;
import com.qf.emp.utils.DbUtils;
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
