package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.service;

import com.qf.ems.entity.Emp;
import com.qf.ems.entity.Page;

import java.util.List;

public interface EmpService {
    public List<Emp> showAllEmpByPage(Page page);
    public int deleteEmp(int id);
    public int addEmp(Emp emp);
    public Emp selectEmpById(int id);
    public int modifyEmp(Emp emp);
}
