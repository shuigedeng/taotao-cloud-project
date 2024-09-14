package com.taotao.boot.data.analysis.annotation.mapper;

import com.taotao.boot.data.analysis.annotation.PostgreSQL;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@PostgreSQL
public interface PostgreSQLMapper extends BaseMapper<User> {

    User selectByUsername(@Param("username") String username);

}
