package com.taotao.cloud.java.javaweb.p2_jdbc.druid.jdbc;


import com.taotao.cloud.java.javaweb.p2_jdbc.druid.domain.Employee;
import com.taotao.cloud.java.javaweb.p2_jdbc.druid.dao.EmployeeDao;
import com.taotao.cloud.java.javaweb.p2_jdbc.druid.dao.impl.EmployeeDaoImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description 测试dao
 */
public class Demo2 {

    public static void main(String[] args) throws SQLException {
        EmployeeDao employeeDao=new EmployeeDaoImpl();
        //Employee emp=new Employee(9999,"Tom","CLERK",7902, Date.valueOf("2015-10-10"),3500.0,500.0,20);

        //employeeDao.addEmployee(emp);

        List<Employee> emps=employeeDao.findByPage(1,5);
        for(Employee emp:emps){
            System.out.println(emp);
        }


    }
}
