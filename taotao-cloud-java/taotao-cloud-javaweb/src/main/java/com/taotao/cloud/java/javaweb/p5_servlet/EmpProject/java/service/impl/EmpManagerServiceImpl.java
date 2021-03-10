package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service.impl;


import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.dao.EmpManagerDao;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.dao.impl.EmpManagerDaoImpl;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.entity.EmpManager;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service.EmpManagerService;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.utils.DbUtils;

public class EmpManagerServiceImpl implements EmpManagerService {
    private EmpManagerDao empManagerDao = new EmpManagerDaoImpl();
    @Override
    public EmpManager login(String username, String password) {
        EmpManager empManager = null;
        try {
            DbUtils.begin();
            EmpManager temp = empManagerDao.select(username);
            if(temp!=null){
                if(temp.getPassword().equals(password)){
                    empManager = temp;
                }
            }
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return empManager;
    }
}
