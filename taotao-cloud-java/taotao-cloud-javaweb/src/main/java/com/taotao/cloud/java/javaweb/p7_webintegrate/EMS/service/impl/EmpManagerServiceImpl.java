package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.service.impl;

import com.qf.ems.dao.EmpManagerDao;
import com.qf.ems.dao.impl.EmpManagerDaoImpl;
import com.qf.ems.entity.EmpManager;
import com.qf.ems.service.EmpManagerService;
import com.qf.ems.utils.DbUtils;

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
