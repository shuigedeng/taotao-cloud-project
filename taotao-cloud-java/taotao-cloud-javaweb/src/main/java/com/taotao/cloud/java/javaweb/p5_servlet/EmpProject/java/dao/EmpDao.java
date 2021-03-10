package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.dao;


import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.entity.Emp;
import java.util.List;

public interface EmpDao {
    public List<Emp> selectAll();

    public int delete(int id);

    public int update(Emp emp);

    public Emp select(int id);


}
