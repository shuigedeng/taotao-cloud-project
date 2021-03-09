package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.service.impl;

import com.qf.emp.dao.EmpManagerDao;
import com.qf.emp.dao.impl.EmpManagerDaoImpl;
import com.qf.emp.entity.EmpManager;
import com.qf.emp.service.EmpManagerService;
import com.qf.emp.utils.DbUtils;

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
