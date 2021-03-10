package com.taotao.cloud.java.javaweb.p12_myshop.dao.impl;

import com.taotao.cloud.java.javaweb.p12_myshop.dao.UserDao;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.User;
import com.taotao.cloud.java.javaweb.p12_myshop.utils.C3P0Utils;
import com.taotao.cloud.java.javaweb.p12_myshop.utils.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * 数据库访问实现类
 */
public class UserDaoImpl implements UserDao {

    @Override
    public User selectUserByUname(String username) throws SQLException {
        //1.创建一个QueryRunner对象
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
        //2.执行sql语句
        String  sql = "select u_id as uid , u_name as username , u_password as upassword" +
                ", u_sex as usex , u_status as ustatus , u_code as code , u_email as email " +
                ", u_role as urole from user where u_name = ?";
        User user = queryRunner.query(sql, new BeanHandler<User>(User.class), username);

        return user;
    }

    @Override
    public int insertUser(User user) throws SQLException {
        //1.创建一个QueryRunner对象
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        //2.插入数据
        String sql = "insert into user (u_name,u_password,u_sex,u_status," +
                "u_code,u_email,u_role) value (?,?,?,?,?,?,?)";

        int rows = queryRunner.update(sql, user.getUsername(), user.getUpassword(), user.getUsex(),
                user.getUstatus(), user.getCode(), user.getEmail(), user.getUrole());
        return rows;
    }

    @Override
    public User selectUserByCode(String code) throws SQLException {

        //1.创建一个QueryRunner对象
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
        //2.执行sql语句
        String  sql = "select u_id as uid , u_name as username , u_password as upassword" +
                ", u_sex as usex , u_status as ustatus , u_code as code , u_email as email " +
                ", u_role as urole from user where u_code = ?";
        User user = queryRunner.query(sql, new BeanHandler<User>(User.class), code);

        return user;

    }

    @Override
    public int updateStatusByUid(int uid) throws SQLException {

        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        String sql = "update user set u_status = ? where u_id = ?";

        int row = queryRunner.update(sql, Constants.USER_ACTIVE, uid);
        return row;
    }
}
