package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.test;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.dao.SubjectDAO;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity.Student2;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity.Subject;
import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.util.MyBatisUtil;
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
