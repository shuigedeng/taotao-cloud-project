package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.java.dao;

import com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatisadvance.java.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDAO {

    List<User> queryUsers(@Param("rule") String rule);// desc asc

    User queryUserById(@Param("id") Integer id);// #{arg0}  ${arg0}
    User queryUserById2(@Param("id") Integer id);// #{arg0}  ${arg0}

    List<User> queryUserByUsername(@Param("username") String username);

    List<User> queryUserByUsernameorId(User user); // ${username}  ${id}

    /*@Select("SELECT id,username,password,gender,regist_time FROM t_user")
    List<User> queryUsers();
    @Select("SELECT id,username,password,gender,regist_time\n" +
            " FROM t_user\n" +
            " WHERE id = #{id}")
    User queryUserById(@Param("id") Integer id);

    @Delete("delete from t_user\n" +
            "        where id=#{id}")
    Integer deleteUser(@Param("id") Integer id);

    @Update("update t_user\n" +
            "        set username=#{username},password=#{password},gender=#{gender},regist_time=#{registTime}\n" +
            "        where id=#{id}")
    Integer updateUser(User user);

    @Options(useGeneratedKeys = true , keyProperty = "id") // 自增key，主键为id
    @Insert("insert into t_user values(#{id},#{username},#{password},#{gender},#{registTime})")
    Integer insertUser(User user);*/
}
