package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.java.dao;

import com.qf.entity.Passport;
import org.apache.ibatis.annotations.Param;

public interface PassportDAO {


    Passport queryPassportById(@Param("id") Integer id);
}
