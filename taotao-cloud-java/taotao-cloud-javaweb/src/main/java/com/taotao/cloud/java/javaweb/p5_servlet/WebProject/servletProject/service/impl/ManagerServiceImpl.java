package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servletProject.service.impl;

import com.qf.servletProject.dao.ManagerDao;
import com.qf.servletProject.dao.impl.ManagerDaoImpl;
import com.qf.servletProject.entity.Manager;
import com.qf.servletProject.service.ManagerService;
import com.qf.servletProject.utils.DbUtils;

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
