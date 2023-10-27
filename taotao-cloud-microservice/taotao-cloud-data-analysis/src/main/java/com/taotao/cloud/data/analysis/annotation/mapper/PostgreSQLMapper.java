package com.taotao.cloud.data.analysis.annotation.mapper;

import com.taotao.cloud.data.analysis.annotation.PostgreSQL;

@Mapper
@PostgreSQL
public interface PostgreSQLMapper extends BaseMapper<User> {

    User selectByUsername(@Param("username") String username);

}
