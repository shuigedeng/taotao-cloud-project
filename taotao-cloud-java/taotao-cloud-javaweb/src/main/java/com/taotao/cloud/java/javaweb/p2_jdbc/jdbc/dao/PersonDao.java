package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.dao;


import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.entity.Person;

import java.util.List;

public interface PersonDao {
    public int insert(Person person);

    public int update(Person person);

    public int delete(int id);

    public Person select(int id);

    public List<Person> selectAll();
}
