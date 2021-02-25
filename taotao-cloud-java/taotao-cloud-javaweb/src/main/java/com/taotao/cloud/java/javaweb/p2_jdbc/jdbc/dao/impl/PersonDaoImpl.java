package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.dao.impl;


import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.advanced.impl.PersonRowMapper;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.dao.PersonDao;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.entity.Person;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.utils.DaoUtils;
import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.utils.DateUtils;

import java.util.List;

public class PersonDaoImpl implements PersonDao {
    private DaoUtils<Person> daoUtils = new DaoUtils();

    @Override
    public int insert(Person person) {
        String sql = "insert into person(name,age,borndate,email,address) values(?,?,?,?,?);";
        Object[] args = {person.getName(), person.getAge(), DateUtils.utilToSql(person.getBornDate()), person.getEmail(), person.getAddress()};
        return daoUtils.commonsUpdate(sql, args);
    }

    @Override
    public int update(Person person) {
        String sql = "update person set name=?,age=?,borndate=?,email=?,address = ? where id = ?";
        Object[] args = {person.getName(), person.getAge(), DateUtils.utilToSql(person.getBornDate()), person.getEmail(), person.getAddress(), person.getId()};
        return daoUtils.commonsUpdate(sql, args);
    }

    @Override
    public int delete(int id) {
        String sql = "delete from person where id = ?";
        return daoUtils.commonsUpdate(sql, id);
    }

    @Override
    public Person select(int id) {
        String sql = "select * from person where id = ?";
        List<Person> list = daoUtils.commonsSelect(sql, new PersonRowMapper(), id);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Person> selectAll() {
        String sql = "select * from person;";
        List<Person> list = daoUtils.commonsSelect(sql, new PersonRowMapper(), null);
        return list;
    }
}
