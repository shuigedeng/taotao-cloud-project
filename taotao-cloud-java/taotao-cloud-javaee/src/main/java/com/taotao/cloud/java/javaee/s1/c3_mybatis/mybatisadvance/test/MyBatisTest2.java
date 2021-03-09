package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.test;

import com.qf.dao.EmployeeDAO;
import com.qf.entity.Department;
import com.qf.entity.Employee;
import com.qf.entity.User;
import com.qf.util.MyBatisUtil;
import org.junit.Test;

public class MyBatisTest2 {
    @Test
    public void test1(){
        /*DepartmentDAO mapper = MyBatisUtil.getMapper(DepartmentDAO.class);
        Department department = mapper.queryDepartmentById(1);
        System.out.println(department);
        List<Employee> employees = department.getEmployees();
        for (Employee employee : employees) {
            System.out.println(employee);
        }*/

        EmployeeDAO mapper = MyBatisUtil.getMapper(EmployeeDAO.class);
        Employee employee = mapper.queryEmployeeById(1);
//        System.out.println(employee);
        System.out.println(employee.getDepartment());
    }
}
