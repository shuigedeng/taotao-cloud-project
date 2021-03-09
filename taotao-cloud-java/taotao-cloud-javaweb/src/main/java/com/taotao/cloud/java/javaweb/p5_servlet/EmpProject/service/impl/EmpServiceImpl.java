package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.service.impl;

import com.qf.emp.dao.EmpDao;
import com.qf.emp.dao.impl.EmpDaoImpl;
import com.qf.emp.entity.Emp;
import com.qf.emp.service.EmpService;
import com.qf.emp.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;

public class EmpServiceImpl implements EmpService {
    private EmpDao empDao = new EmpDaoImpl();
    @Override
    public List<Emp> showAllEmp() {
        List<Emp> emps = new ArrayList<>();
        try {
            DbUtils.begin();
            List<Emp> temps = empDao.selectAll();
            if(temps!=null){
                emps = temps;
            }
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return emps;
    }

    @Override
    public int removeEmp(int id) {
        int result = 0;
        try {
            DbUtils.begin();
            result = empDao.delete(id);
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int modify(Emp emp) {
        int result = 0;
        try {
            DbUtils.begin();
            result = empDao.update(emp);
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return result ;
    }

    @Override
    public Emp showEmp(int id) {
        Emp emp = null;
        try {
            DbUtils.begin();
            emp = empDao.select(id);
            DbUtils.commit();
        } catch (Exception e) {
            DbUtils.rollback();
            e.printStackTrace();
        }
        return emp;
    }

}
