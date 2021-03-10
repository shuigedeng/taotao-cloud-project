package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.service.impl;


import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.dao.ManagerDao;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.dao.impl.ManagerDaoImpl;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.entity.Manager;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.service.ManagerService;
import com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.utils.DbUtils;

public class ManagerServiceImpl implements ManagerService {
    private ManagerDao managerDao = new ManagerDaoImpl();
    @Override
    public Manager login(String username, String password) {
        Manager manager = null;
        try {
            DbUtils.begin();
            Manager temp = managerDao.select(username);
            if(temp!=null){
                if(temp.getPassword().equals(password)){
                    manager = temp;
                }
            }
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return manager;
    }
}
