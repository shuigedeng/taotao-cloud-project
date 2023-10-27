package com.taotao.cloud.data.analysis.annotation.mapper;

import com.taotao.cloud.data.analysis.annotation.MySQL;

@Mapper
@MySQL
public interface MySQLMapper extends BaseMapper<User> {

    User selectByUsername(@Param("username") String username);

}
