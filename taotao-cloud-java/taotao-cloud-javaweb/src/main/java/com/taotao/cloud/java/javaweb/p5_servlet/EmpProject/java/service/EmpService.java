package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service;


import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.entity.Emp;
import java.util.List;

public interface EmpService {
    public List<Emp> showAllEmp();

    public int removeEmp(int id);

    public int modify(Emp emp);

    public Emp showEmp(int id);

}
