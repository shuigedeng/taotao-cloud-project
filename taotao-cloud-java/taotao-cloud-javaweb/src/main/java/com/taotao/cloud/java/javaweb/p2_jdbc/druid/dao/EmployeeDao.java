package com.taotao.cloud.java.javaweb.p2_jdbc.druid.dao;

import com.taotao.cloud.java.javaweb.p2_jdbc.druid.domain.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao {

    //添加员工
    void addEmployee(Employee emp)throws SQLException;

    //根据编号删除员工
    void delEmployee(int empno)throws SQLException;

    //修改员工信息
    void modifyEmployee(Employee emp)throws SQLException;

    //查找员工根据编号
    Employee findById(int empno)throws SQLException;

    //查找所有员工
    List<Employee> findAll()throws SQLException;

    //分页查询
    List<Employee> findByPage(int page,int pageSize)throws SQLException;
}












