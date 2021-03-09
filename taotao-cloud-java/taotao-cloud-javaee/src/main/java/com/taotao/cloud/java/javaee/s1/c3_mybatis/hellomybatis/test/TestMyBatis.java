package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.test;

import com.qf.dao.*;
import com.qf.entity.*;
import com.qf.util.MyBatisUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.List;

public class TestMyBatis {
    public static void main(String[] args) throws IOException {
        SubjectDAO mapper = MyBatisUtil.getMapper(SubjectDAO.class);
        Subject subject = mapper.querySubjectById(1001);
        System.out.println(subject);
        List<Student2> students = subject.getStudents();
        for (Student2 student : students) {
            System.out.println(student);
        }
    }
}
