package com.taotao.cloud.data.analysis.annotation.mapper;

import com.taotao.cloud.data.analysis.annotation.MySQL;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
@MySQL
public interface MySQLMapper extends BaseMapper<User> {

    User selectByUsername(@Param("username") String username);

}
