package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.service;


import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Emp;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Page;
import java.util.List;

public interface EmpService {
    public List<Emp> showAllEmpByPage(Page page);
    public int deleteEmp(int id);
    public int addEmp(Emp emp);
    public Emp selectEmpById(int id);
    public int modifyEmp(Emp emp);
}
