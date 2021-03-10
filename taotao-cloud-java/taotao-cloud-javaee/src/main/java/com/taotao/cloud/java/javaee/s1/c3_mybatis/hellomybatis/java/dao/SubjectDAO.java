package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.dao;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.entity.Subject;
import org.apache.ibatis.annotations.Param;

public interface SubjectDAO {

    Subject querySubjectById(@Param("id") Integer id);
}
