package com.taotao.cloud.java.javaweb.p12_myshop.dao.impl;

import com.itqf.dao.ProductDao;
import com.itqf.entity.Product;
import com.itqf.utils.C3P0Utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public long selectCountByTid(String tid) throws SQLException {

        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        String sql = "select count(1) from product where t_id = ?";

        Object result = queryRunner.query(sql, new ScalarHandler(),tid);

        Long total = (Long) result;
        return total;
    }

    @Override
    public List<Product> selectProductByPage(int page, int pageSize, String tid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        String sql = "select p_id as pid,t_id as tid,p_name as pname,p_time as ptime ," +
                "p_image as pimage,p_state as pstate ,p_info as pinfo ,p_price as pprice " +
                "from product where t_id = ? limit ?,?;";

        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class),
                tid, (page - 1) * pageSize, pageSize);

        return list;
    }

    @Override
    public Product selectProductByPid(String pid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        String sql = "select p_id as pid,t_id as tid,p_name as pname,p_time as ptime ," +
                "p_image as pimage,p_state as pstate ,p_info as pinfo ,p_price as pprice " +
                "from product where p_id = ?;";

        Product product = queryRunner.query(sql, new BeanHandler<Product>(Product.class), pid);
        return product;
    }
}
