package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.java.dao;

import com.qf.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeDAO {

    // 查询员工信息 并且 查到对应的部门信息
    Employee queryEmployeeById(@Param("id") Integer id);

    // 查询某个部门下的所有员工
    List<Employee> queryEmployeeByDeptId(@Param("deptId") Integer deptId);
}
