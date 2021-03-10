package com.taotao.cloud.java.javaweb.p12_myshop.service;


import com.taotao.cloud.java.javaweb.p12_myshop.entity.Address;
import java.sql.SQLException;
import java.util.List;

public interface AddressService {
    List<Address> findAddressByUid(int uid) throws SQLException;

    void saveAddress(Address address) throws SQLException;

    void deleteAddressByAid(String aid) throws SQLException;

    void setAddressToDefault(String aid, int uid) throws SQLException;

    void updateByAid(Address address) throws SQLException;
}
