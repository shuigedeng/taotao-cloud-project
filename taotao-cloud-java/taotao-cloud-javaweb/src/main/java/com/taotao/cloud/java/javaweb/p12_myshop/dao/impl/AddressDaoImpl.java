package com.taotao.cloud.java.javaweb.p12_myshop.dao.impl;

import com.itqf.dao.AddressDao;
import com.itqf.entity.Address;
import com.itqf.utils.C3P0Utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class AddressDaoImpl implements AddressDao {


    @Override
    public List<Address> selectAddressByUid(int uid) throws SQLException {

        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        String sql = "select a_id as aid, u_id as uid,a_name as aname,a_phone " +
                "as aphone,a_detail as adetail ,a_state as astate from " +
                "address where u_id = ? order by a_state desc;";

        List<Address> list = queryRunner.query(sql, new BeanListHandler<Address>(Address.class), uid);
        return list;
    }

    @Override
    public void insertAddress(Address address) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "insert into address (u_id,a_name,a_phone,a_detail,a_state) value(?,?,?,?,?)";

        queryRunner.update(sql, address.getUid(),address.getAname(),
                address.getAphone(),address.getAdetail(),address.getAstate());
    }

    @Override
    public void deleteAddressByAid(String aid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());

        String sql = "delete from address where a_id = ?;";

        queryRunner.update(sql, aid);
    }

    @Override
    public void updateAddressToDefault(String aid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "update address set a_state = 1 where a_id = ?";
        queryRunner.update(sql, aid);
    }

    @Override
    public void updateAddressToCommons(String aid, int uid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "update address set a_state = 0 where a_id != ? and u_id = ?";
        queryRunner.update(sql, aid,uid);
    }

    @Override
    public void updateAddressByAid(Address address) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "update address set a_state = ?,a_name=?,a_phone=?,a_detail = ? where a_id = ?;";
        queryRunner.update(sql, address.getAstate(),address.getAname(),address.getAphone(),address.getAdetail(),address.getAid());
    }


}
